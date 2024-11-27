"use client";

import ResumenCargaDeHoras from "@/_componentes/ResumenCargaDeHoras";
import { RecursoActualContext } from "@/_context/recursoActualContext";
import { Proyecto, Tarea } from "@/_lib/tipos";
import { DevTool } from "@hookform/devtools";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Alert,
  Button,
  Datepicker,
  Label,
  Modal,
  Select,
  TextInput
} from "flowbite-react";
import React, { useContext, useEffect, useMemo, useState } from "react";
import { Control, Controller, FieldErrors, useForm } from "react-hook-form";
import { z } from "zod";
import { cargarHoras as cargarHorasAction } from "./action";

const schema = z.object({
  proyectoId: z.string(),
  tareaId: z.string(),
  cantidadHoras: z
    .number({
      required_error: "Requerido"
    })
    .gt(0, {
      message: "Cantidad de horas debe ser mayor a 0."
    })
    .lte(24, {
      message: "Cantidad de horas debe ser menor a 24."
    }),
  fechaCarga: z.date()
});

type Schema = z.infer<typeof schema>;

export default function Formulario({
  proyectos,
  tareas
}: {
  proyectos: Proyecto[];
  tareas: Tarea[];
}) {
  const [modalAbierto, setModalAbierto] = useState(false);
  const [esperandoRespuesta, setEsperandoRespuesta] = useState(false);
  const [resultadoServerAction, setResultadoServerAction] = useState<Awaited<
    ReturnType<typeof cargarHorasAction>
  > | null>(null);

  const recursoActualContext = useContext(RecursoActualContext);
  if (!recursoActualContext || !recursoActualContext.state?.recursoActual) {
    return null;
  }
  const recursoActual = recursoActualContext.state.recursoActual;

  const proyectosConTareas = useMemo(() => {
    return proyectos.filter((proyecto) =>
      tareas.some((tarea) => tarea.proyectoId === proyecto.id)
    );
  }, [proyectos, tareas]);
  const proyectoPorDefecto = proyectosConTareas[0];

  const {
    control,
    formState: { errors },
    getValues,
    handleSubmit,
    setError,
    watch,
    reset,
    setValue
  } = useForm<Schema>({
    resolver: zodResolver(schema),
    defaultValues: {
      proyectoId: proyectoPorDefecto.id,
      tareaId: tareas.find((t) => t.proyectoId === proyectoPorDefecto.id)!.id,
      cantidadHoras: 0,
      fechaCarga: new Date()
    }
  });

  const proyectoId = watch("proyectoId");
  const proyectoTieneTareas = useMemo(
    () => tareas.some((t) => t.proyectoId === proyectoId),
    [tareas, proyectoId]
  );

  useEffect(() => {
    const tareasDelProyecto = tareas.filter((t) => t.proyectoId === proyectoId);
    if (tareasDelProyecto.length > 0) {
      reset({
        ...getValues(),
        proyectoId: proyectoId,
        tareaId: tareasDelProyecto[0].id
      });
    } else {
      setError("proyectoId", {
        type: "manual",
        message: "Este proyecto no tiene tareas asociadas."
      });
    }
  }, [proyectoId, tareas, setValue]);

  function handleAbrirModal() {
    handleSubmit(() => {
      setModalAbierto(true);
    })();
  }

  async function handleEnviarFormulario() {
    const data = getValues();
    const formData = new FormData();

    formData.append("tareaId", data.tareaId);
    formData.append("recursoId", recursoActual.id);
    formData.append("cantidadHoras", data.cantidadHoras.toString());
    formData.append(
      "fechaCarga",
      data.fechaCarga.toLocaleDateString("es-AR", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric"
      })
    );

    setEsperandoRespuesta(true);
    setResultadoServerAction(await cargarHorasAction(formData));
    setEsperandoRespuesta(false);

    setModalAbierto(false);
  }

  return (
    <>
      <form className="space-y-4" onSubmit={(e) => e.preventDefault()}>
        {resultadoServerAction && (
          <Alert
            color={resultadoServerAction.exito ? "success" : "failure"}
            onDismiss={() => setResultadoServerAction(null)}
          >
            <span className="font-medium">{resultadoServerAction.mensaje}</span>
          </Alert>
        )}

        <div className="grid gap-4 grid-cols-4">
          <div className="col-span-4 space-y-2">
            <Label htmlFor="proyectoId">Seleccione un proyecto</Label>
            <Controller
              name="proyectoId"
              control={control}
              render={({ field }) => (
                <Select
                  id="proyectoId"
                  {...field}
                  color={errors.proyectoId && "failure"}
                  helperText={
                    errors.proyectoId && (
                      <span className="font-medium">
                        {errors.proyectoId.message}
                      </span>
                    )
                  }
                >
                  {proyectos.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.id} - {p.nombre}
                    </option>
                  ))}
                </Select>
              )}
            />
          </div>

          <div className="col-span-4">
            <Label htmlFor="tareaId">Seleccione una tarea</Label>
            <Controller
              name="tareaId"
              control={control}
              render={({ field }) => (
                <Select
                  id="taredId"
                  {...field}
                  color={errors.tareaId && "failure"}
                  disabled={!proyectoTieneTareas}
                  helperText={
                    errors.tareaId && (
                      <span className="font-medium">
                        {errors.tareaId.message}
                      </span>
                    )
                  }
                >
                  {tareas
                    .filter((t) => t.proyectoId === proyectoId)
                    .map((t) => (
                      <option key={t.id} value={t.id}>
                        {t.id} - {t.nombre}
                      </option>
                    ))}
                </Select>
              )}
            />
          </div>

          <div className="col-span-2">
            <Label htmlFor="fechaCarga">Seleccione la fecha de la carga</Label>
            <Controller
              name="fechaCarga"
              control={control}
              render={({ field }) => (
                <Datepicker
                  id="fechaCarga"
                  language="es-AR"
                  labelTodayButton="Hoy"
                  labelClearButton="Limpiar"
                  maxDate={new Date()}
                  {...field}
                  color={errors.fechaCarga && "failure"}
                  disabled={!proyectoTieneTareas}
                  helperText={
                    errors.fechaCarga && (
                      <span className="font-medium">
                        {errors.fechaCarga.message}
                      </span>
                    )
                  }
                />
              )}
            />
          </div>

          <div className="col-span-1">
            <Label htmlFor="cantidadHoras">
              Seleccione la fecha de la carga
            </Label>
            <Controller
              name="cantidadHoras"
              control={control}
              render={({ field }) => (
                <TextInput
                  id="cantiadHoras"
                  type="number"
                  {...field}
                  value={field.value || ""}
                  onChange={(e) =>
                    field.onChange(
                      e.target.value === "" ? 0 : parseFloat(e.target.value)
                    )
                  }
                  color={errors.cantidadHoras && "failure"}
                  disabled={!proyectoTieneTareas}
                  helperText={
                    errors.cantidadHoras && (
                      <span className="font-medium">
                        {errors.cantidadHoras.message}
                      </span>
                    )
                  }
                />
              )}
            />
          </div>
        </div>

        <div className="flex justify-end">
          <Button onClick={handleAbrirModal} disabled={!proyectoTieneTareas}>
            Cargar horas
          </Button>
        </div>

        <Modal show={modalAbierto} onClose={() => setModalAbierto(false)}>
          <Modal.Header>Confirmar carga de horas</Modal.Header>
          <Modal.Body>
            <ResumenCargaDeHoras
              proyecto={proyectos.find((p) => p.id === getValues().proyectoId)!}
              tarea={tareas.find((t) => t.id === getValues().tareaId)!}
              cantidadHoras={getValues().cantidadHoras}
              fechaCarga={getValues().fechaCarga}
            />
          </Modal.Body>
          <Modal.Footer className="justify-end">
            <Button
              onClick={handleEnviarFormulario}
              isProcessing={esperandoRespuesta}
            >
              Confirmar
            </Button>
            <Button
              color="gray"
              onClick={() => setModalAbierto(false)}
              disabled={esperandoRespuesta}
            >
              Cancelar
            </Button>
          </Modal.Footer>
        </Modal>
      </form>
      <DevTool control={control} />
    </>
  );
}

function InputFormulario({
  nombreInput,
  labelInput,
  className,
  control,
  proyectoTieneTareas,
  errors,
  children
}: {
  nombreInput: keyof Schema;
  labelInput: string;
  className: string;
  control: Control<Schema>;
  proyectoTieneTareas: boolean;
  errors: FieldErrors<Schema>;
  children: React.ReactElement;
}) {
  return (
    <div className={className}>
      <Label htmlFor={nombreInput} value={labelInput} />
      <Controller
        name={nombreInput}
        control={control}
        render={({ field }) =>
          React.cloneElement(children, {
            ...field,
            id: nombreInput,
            name: nombreInput,
            disabled:
              nombreInput === "proyectoId" ? false : !proyectoTieneTareas,
            color: errors[nombreInput] && "failure",
            helperText: errors[nombreInput] && (
              <span className="font-medium">{errors[nombreInput].message}</span>
            ),
            value:
              nombreInput === "cantidadHoras"
                ? field.value === undefined
                  ? ""
                  : field.value
                : field.value,
            onChange:
              nombreInput === "cantidadHoras"
                ? (e: React.ChangeEvent<HTMLInputElement>) => {
                    const value = e.target.value;
                    field.onChange(value === "" ? undefined : Number(value));
                  }
                : field.onChange
          })
        }
      />
    </div>
  );
}
