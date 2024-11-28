"use client";

import { desencodearFecha, encodearFecha } from "@/_lib/fecha";
import { Proyecto } from "@/_lib/tipos";
import { DevTool } from "@hookform/devtools";
import { Datepicker, Label, Select } from "flowbite-react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useEffect } from "react";
import { Controller, useForm } from "react-hook-form";

interface ValoresFormulario {
  proyectoId: string;
  fechaInicio: Date;
  fechaFin: Date;
}

export default function Formulario({
  proyectos,
  children
}: {
  proyectos: Proyecto[];
  children: React.ReactNode;
}) {
  const router = useRouter();
  const pathName = usePathname();
  const searchParams = useSearchParams();

  const defaultProyectoId = pathName.split("/").at(-1);

  const { control, watch, setValue } = useForm<ValoresFormulario>({
    defaultValues: {
      proyectoId: defaultProyectoId || proyectos[0].id,
      fechaInicio: desencodearFecha(searchParams.get("fechaInicio")!),
      fechaFin: desencodearFecha(searchParams.get("fechaFin")!)
    }
  });

  const fechaInicio = watch("fechaInicio");
  const fechaFin = watch("fechaFin");
  const proyectoId = watch("proyectoId");

  useEffect(() => {
    if (proyectoId && fechaInicio && fechaFin) {
      router.push(
        `/proyectos/${proyectoId}?fechaInicio=${encodearFecha(fechaInicio)}&fechaFin=${encodearFecha(fechaFin)}`
      );
    }
  }, [proyectoId, fechaInicio, fechaFin, router]);

  const handleCambioFechaFin = (fecha: Date) => {
    if (fechaInicio > fecha) {
      setValue("fechaInicio", fecha);
    }
    setValue("fechaFin", fecha, { shouldDirty: true });
  };

  return (
    <>
      <div className="space-y-2">
        <Label htmlFor="proyectoId" value="Seleccione un proyecto" />
        <Controller
          name="proyectoId"
          control={control}
          render={({ field }) => (
            <Select id="proyectoId" {...field}>
              {proyectos.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.id} - {p.nombre}
                </option>
              ))}
            </Select>
          )}
        />
      </div>

      <div className="space-y-4">
        <Label value="Seleccione un rango de fechas" />
        <div className="flex flex-col md:flex-row justify-center items-center gap-4">
          <span>Desde el</span>
          <Controller
            name="fechaInicio"
            control={control}
            render={({ field }) => (
              <Datepicker
                {...field}
                className="w-80"
                language="es-AR"
                labelTodayButton="Hoy"
                labelClearButton="Limpiar"
                maxDate={fechaFin || new Date()}
              />
            )}
          />
          <span>hasta el</span>
          <Controller
            name="fechaFin"
            control={control}
            render={({ field }) => (
              <Datepicker
                {...field}
                className="w-80"
                language="es-AR"
                labelTodayButton="Hoy"
                labelClearButton="Limpiar"
                maxDate={new Date()}
                onChange={(fecha) => {
                  field.onChange(fecha);
                  handleCambioFechaFin(fecha as Date);
                }}
              />
            )}
          />
        </div>
        <DevTool control={control} />
      </div>
      {children}
    </>
  );
}
