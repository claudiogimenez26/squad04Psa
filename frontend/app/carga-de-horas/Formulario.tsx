"use client";

import React, { useContext } from "react";
import { Label, Select, Datepicker, TextInput, Button } from "flowbite-react";
import { Proyecto, Tarea } from "@/app/lib/types";
import { LoginContext } from "../context/LoginContext";

export default function ({
  proyectos,
  tareas,
}: {
  proyectos: Proyecto[];
  tareas: Tarea[];
}) {
  const loginContext = useContext(LoginContext);
  if (!loginContext) {
    throw new Error("El componente debe estar dentro de LoginContextProvider");
  }
  const { state: recurso } = loginContext;

  const [proyectoId, setProyectoId] = React.useState<string>(proyectos[0].id);
  const [tareaId, setTareaId] = React.useState<string>(tareas[0]?.id);
  const [fecha, setFecha] = React.useState<Date>(new Date());
  const [cantidadHoras, setCantidadHoras] = React.useState<number>(0);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/cargar-horas", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          tareaId,
          fechaDeCarga: fecha.toISOString().split("T")[0],
          cantidadHoras: Number(cantidadHoras),
          recursoId: recurso.id,
        }),
      });

      if (!response.ok) {
        throw new Error("Error al cargar las horas");
      }

      setCantidadHoras(0);
      setFecha(new Date());

      alert("Horas cargadas exitosamente");
    } catch (error) {
      console.error("Error:", error);
      alert("Error al cargar las horas");
    }
  };

  return (
    <form
      className="h-full flex flex-col justify-between"
      onSubmit={handleSubmit}
    >
      <div className="grid gap-4 grid-cols-4">
        <div className="col-span-4 space-y-2">
          <Label htmlFor="proyectos" value="Seleccione un proyecto" />
          <Select
            id="proyectos"
            name="proyectoId"
            value={proyectoId}
            onChange={(e) => setProyectoId(e.target.value)}
          >
            {proyectos.map((p) => (
              <option key={p.id} value={p.id}>
                {p.id} - {p.nombre}
              </option>
            ))}
          </Select>
        </div>

        <div className="col-span-4 space-y-2">
          <Label htmlFor="tareas" value="Seleccione una tarea" />
          <Select
            id="tareas"
            name="tareaId"
            value={tareaId}
            onChange={(e) => setTareaId(e.target.value)}
            required
          >
            {tareas
              .filter((t) => t.proyectoId === proyectoId)
              .map((t) => (
                <option key={t.id} value={t.id}>
                  {t.id} - {t.nombre}
                </option>
              ))}
          </Select>
        </div>

        <div className="col-span-2 space-y-2">
          <Label htmlFor="fecha-carga" value="Seleccione fecha de carga" />
          <Datepicker
            name="fechaDeCarga"
            language="es-AR"
            labelTodayButton="Hoy"
            labelClearButton="Limpiar"
            maxDate={new Date()}
            value={fecha}
            onChange={(fecha) => {
              if (fecha) {
                setFecha(fecha);
              }
            }}
          />
        </div>

        <div className="col-span-1 space-y-2">
          <Label htmlFor="cantidadHoras" value="Ingrese la cantidad de horas" />
          <TextInput
            id="cantidadHoras"
            name="cantidadHoras"
            type="number"
            value={cantidadHoras}
            onChange={(e) => setCantidadHoras(Number(e.target.value))}
            required
          />
        </div>
      </div>

      <div className="">
        <Button type="submit">Cargar Horas</Button>
      </div>
    </form>
  );
}
