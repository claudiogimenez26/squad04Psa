"use client";

import { RecursoContext } from "@/_context/recursoContext";
import { Proyecto, Tarea } from "@/_lib/tipos";
import { cargarHoras } from "@/cargar-horas/actions";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button, Datepicker, Label, Select, TextInput } from "flowbite-react";
import React, { useContext, useEffect, useMemo } from "react";
import { Control, Controller, FieldErrors, useForm } from "react-hook-form";
import { z } from "zod";

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

type FormValues = z.infer<typeof schema>;

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
    control,
    handleSubmit,
    watch,
    setError,
    clearErrors,
    formState: { errors }
  } = useForm<FormValues>({
    resolver: zodResolver(schema),
    defaultValues: {
      proyectoId: proyectoInicial.id,
      tareaId: tareas.find((t) => t.proyectoId === proyectoInicial.id)!.id,
      cantidadHoras: 0,
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

  const onSubmit = async (data: FormValues) => {
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

    await cargarHoras(formData);
  };

  return (
    <form
      className="flex flex-col justify-between"
      onSubmit={handleSubmit(onSubmit)}
    >
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
        <Button type="submit" disabled={!proyectoTieneTareas}>
          Cargar Horas
        </Button>
      </div>
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
  nombreInput: keyof FormValues;
  labelInput: string;
  className: string;
  control: Control<FormValues>;
  proyectoTieneTareas: boolean;
  errors: FieldErrors<FormValues>;
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
              nombreInput === "cantidadHoras" ? field.value || "" : field.value,
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
