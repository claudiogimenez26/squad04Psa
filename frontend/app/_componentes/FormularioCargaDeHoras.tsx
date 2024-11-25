"use client";

import { RecursoContext } from "@/_context/recursoContext";
import { cargarHoras } from "@/_lib/actions";
import { Proyecto, Tarea } from "@/_lib/tipos";
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
import ResumenCargaDeHoras from "./ResumenCargaDeHoras";

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

type ValoresFormulario = z.infer<typeof schema>;

export default function FormularioCargaDeHoras({
  proyectos,
  tareas
}: {
  proyectos: Proyecto[];
  tareas: Tarea[];
}) {
  const recursoContext = useContext(RecursoContext);

  if (!recursoContext || !recursoContext.state?.recursoActual) {
    return null;
  }

  const recursoActual = recursoContext.state.recursoActual;

  const proyectosConTareas = useMemo(() => {
    return proyectos.filter((proyecto) =>
      tareas.some((tarea) => tarea.proyectoId === proyecto.id)
    );
  }, [proyectos, tareas]);

  const proyectoInicial = proyectosConTareas[0];

  const {
    clearErrors,
    control,
    formState: { errors },
    getValues,
    handleSubmit,
    setError,
    watch
  } = useForm<ValoresFormulario>({
    resolver: zodResolver(schema),
    defaultValues: {
      proyectoId: proyectoInicial.id,
      tareaId: tareas.find((t) => t.proyectoId === proyectoInicial.id)!.id,
      cantidadHoras: undefined,
      fechaCarga: new Date()
    }
  });

  const proyectoId = watch("proyectoId");

  const tareasDelProyecto = useMemo(
    () => tareas.filter((t) => t.proyectoId === proyectoId),
    [tareas, proyectoId]
  );
  const proyectoTieneTareas = useMemo(
    () => tareasDelProyecto.length > 0,
    [tareasDelProyecto]
  );

  useEffect(() => {
    if (!proyectoTieneTareas) {
      setError("proyectoId", {
        type: "manual",
        message: "Este proyecto no tiene tareas asociadas."
      });
    } else {
      clearErrors("proyectoId");
    }
  }, [proyectoId, proyectoTieneTareas, setError, clearErrors]);

  function handleAbrirModal() {
    handleSubmit(() => {
      setModalAbierto(true);
    })();
  }

  const [mensaje, setMensaje] = useState<{
    tipo: "success" | "failure";
    text: string;
  } | null>(null);

  const [esperandoRespuesta, setEsperandoRespuesta] = useState(false);

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
    const resultado = await cargarHoras(formData);
    setEsperandoRespuesta(false);

    if (resultado.success) {
      setMensaje({
        tipo: "success",
        text: "Las horas se cargaron correctamente"
      });
    } else {
      setMensaje({
        tipo: "failure",
        text: resultado.error || "Error al cargar las horas"
      });
    }

    setModalAbierto(false);
  }

  const [modalAbierto, setModalAbierto] = useState(false);

  return (
    <form className="space-y-4" onSubmit={(e) => e.preventDefault()}>
      {mensaje && (
        <Alert color={mensaje.tipo} onDismiss={() => setMensaje(null)}>
          <span className="font-medium">{mensaje.text}</span>
        </Alert>
      )}

      <div className="grid gap-4 grid-cols-4">
        <InputFormulario
          nombreInput="proyectoId"
          labelInput="Seleccione un proyecto"
          className="col-span-4 space-y-2"
          control={control}
          errors={errors}
          proyectoTieneTareas={proyectoTieneTareas}
        >
          <Select id="proyectos">
            {proyectos.map((p) => (
              <option key={p.id} value={p.id}>
                {p.id} - {p.nombre}
              </option>
            ))}
          </Select>
        </InputFormulario>

        <InputFormulario
          nombreInput="tareaId"
          labelInput="Seleccione una tarea"
          className="col-span-4 space-y-2"
          control={control}
          errors={errors}
          proyectoTieneTareas={proyectoTieneTareas}
        >
          <Select>
            {tareas
              .filter((t) => t.proyectoId === proyectoId)
              .map((t) => (
                <option key={t.id} value={t.id}>
                  {t.id} - {t.nombre}
                </option>
              ))}
          </Select>
        </InputFormulario>

        <InputFormulario
          nombreInput="fechaCarga"
          labelInput="Seleccione fecha de carga"
          className="col-span-2 space-y-2"
          control={control}
          errors={errors}
          proyectoTieneTareas={proyectoTieneTareas}
        >
          <Datepicker
            language="es-AR"
            labelTodayButton="Hoy"
            labelClearButton="Limpiar"
            maxDate={new Date()}
          />
        </InputFormulario>

        <InputFormulario
          nombreInput="cantidadHoras"
          labelInput="Ingrese la cantidad de horas"
          className="col-span-2 lg:col-span-1 space-y-2"
          control={control}
          errors={errors}
          proyectoTieneTareas={proyectoTieneTareas}
        >
          <TextInput type="number" />
        </InputFormulario>
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
  nombreInput: keyof ValoresFormulario;
  labelInput: string;
  className: string;
  control: Control<ValoresFormulario>;
  proyectoTieneTareas: boolean;
  errors: FieldErrors<ValoresFormulario>;
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
