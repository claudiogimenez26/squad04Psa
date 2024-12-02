"use client";
import { CargaDeHoras } from "@/_lib/tipos";
import IconoEliminar from "./iconoEliminar";
import IconoModificar from "./iconoModificar";

 // También es un componente interactivo

export default function BloqueCargaDeHoras({
  id,
  tareaNombre,
  cantidadHoras,
  carga,
  onEliminar,
  onModificar
}: {
  id: string;
  tareaNombre: string;
  cantidadHoras: number;
  carga: Partial<CargaDeHoras>;
  onEliminar: (id: string) => void;
  onModificar: (id: string) => void;
  }) {
  const alturaRem = 6.0 + Math.min(cantidadHoras - 1, 23) * 0.75;

  return (
    <div 
      style={{ height: `${alturaRem}rem` }} 
      className="border border-emerald-500 bg-emerald-200 p-2 overflow-y-auto overflow-x-hidden flex flex-col"
    >
      <span className="font-semibold underline">
        {cantidadHoras} hora{cantidadHoras > 1 && "s"}
      </span>
      <span className="text-sm">{tareaNombre}</span>
      <IconoEliminar onEliminar={() => onEliminar(id)} ></IconoEliminar>
      <IconoModificar onModificar={() => onModificar(id)} ></IconoModificar>
    </div>
  );
}