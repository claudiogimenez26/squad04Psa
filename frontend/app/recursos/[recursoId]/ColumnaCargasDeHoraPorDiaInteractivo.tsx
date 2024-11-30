"use client"; // Habilita la interactividad en el cliente

import { eliminarCargaHoras } from "@/recursos/[recursoId]/cargarHoras"; // Asegúrate de que la importación sea correcta
import BloqueCargaDeHoras from "./BloqueCargaDeHoras";
import React from "react";

export default function ColumnaCargasDeHoraPorDiaInteractivo({
  cargasDeHoras: initialCargasDeHoras,
}: {
  cargasDeHoras: any[];
}) {
  const [cargasDeHoras, setCargasDeHoras] = React.useState(initialCargasDeHoras);

  const handleEliminarCarga = async (id: string) => {
    const confirmar = confirm("¿Estás seguro de que deseas eliminar esta carga?");
    if (!confirmar) return;

    try {
      // Llama a la server action para eliminar la carga de horas
      const resultado = await eliminarCargaHoras(id); 

      if (resultado.exito) {
        // Actualiza el estado local para reflejar la eliminación
        setCargasDeHoras((prev) => prev.filter((carga) => carga.id !== id));
        alert(resultado.mensaje);
      } else {
        console.error("Error al eliminar la carga:", resultado.mensaje);
        alert(`Error: ${resultado.mensaje}`);
      }
    } catch (e) {
      console.error("Error inesperado al eliminar la carga:", e);
      alert(`Error inesperado: ${(e as Error).message}`);
    }
  };

  return (
    <div className="flex flex-col gap-1.5">
      {cargasDeHoras.map((carga) => (
        <div key={carga.id}>
          <BloqueCargaDeHoras {...carga} onEliminar={handleEliminarCarga} />
        </div>
      ))}
    </div>
  );
}
