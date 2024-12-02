"use client";

import ResumenCargaDeHoras from "@/_componentes/ResumenCargaDeHoras";
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
import { usePathname } from "next/navigation";
import React, { useState } from "react";
import { Control, Controller, FieldErrors, useForm } from "react-hook-form";
import { z } from "zod";
import { modificarCargaDeHoras } from "./action";

const schema = z.object({
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

export default function FormularioModificacion({
  proyectos,
  tareas,
  cargas
}: {
  proyectos: Proyecto[];
  tareas: Tarea[];
  cargas: {
    idCarga: string;
    nombreTarea: string;
    cantidadHoras: number;
    fechaCarga: string;
    tareaId: string;
    recursoId: string;
  }[];
}) {
  const pathName = usePathname();
  const cargaDeHorasId = pathName.split("/").at(-1);

  const cargaDeHoras = cargas.find((c) => c.idCarga === cargaDeHorasId)!;
  const tarea = tareas.find((t) => t.id === cargaDeHoras.tareaId)!;
  const proyecto = proyectos.find((p) => p.id === tarea.proyectoId)!;

  const [modalAbierto, setModalAbierto] = useState(false);
  const [esperandoRespuesta, setEsperandoRespuesta] = useState(false);
  const [resultadoServerAction, setResultadoServerAction] = useState<Awaited<
    ReturnType<typeof modificarCargaDeHoras>
  > | null>(null);

  const [dia, mes, anio] = cargaDeHoras.fechaCarga.split("/");
  const fechaCarga = new Date(Number(anio), Number(mes) - 1, Number(dia));

  const {
    control,
    formState: { errors },
    getValues,
    handleSubmit
  } = useForm<Schema>({
    resolver: zodResolver(schema),
    defaultValues: {
      cantidadHoras: cargaDeHoras.cantidadHoras,
      fechaCarga
    }
  });

  function handleAbrirModal() {
    handleSubmit(() => {
      setModalAbierto(true);
    })();
  }

  async function handleEnviarFormulario() {
    const data = getValues();
    const formData = new FormData();

    formData.append("cargaId", cargaDeHorasId!.toString());
    formData.append("recursoId", cargaDeHoras.recursoId);
    formData.append("tareaId", tarea.id);
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
    setResultadoServerAction(await modificarCargaDeHoras(formData));
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
            <Select disabled id="proyectoId">
              <option value={proyecto.id}>
                {proyecto.id} - {proyecto.nombre}
              </option>
            </Select>
          </div>

          <div className="col-span-4">
            <Label htmlFor="tareaId">Seleccione una tarea</Label>
            <Select id="taredId" disabled>
              <option value={tarea.id}>
                {tarea.id} - {tarea.nombre}
              </option>
            </Select>
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
            <Label htmlFor="cantidadHoras">Ingrese la cantidad de horas</Label>
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
          <Button onClick={handleAbrirModal}>Modificar carga de horas</Button>
        </div>

        <Modal show={modalAbierto} onClose={() => setModalAbierto(false)}>
          <Modal.Header>Confirmar carga de horas</Modal.Header>
          <Modal.Body>
            <ResumenCargaDeHoras
              proyecto={proyecto}
              tarea={tarea}
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
  errors,
  children
}: {
  nombreInput: keyof Schema;
  labelInput: string;
  className: string;
  control: Control<Schema>;
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
