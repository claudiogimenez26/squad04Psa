"use client";
import { CargaDeHoras } from "@/_lib/tipos";
import IconoEliminar from "./iconoEliminar";
import IconoModificar from "./iconoModificar";


import React from "react";
import { useRouter } from "next/navigation";
import { eliminarCargaHoras, modificarCargadeHoras } from "./action";

export default function BloqueCargaDeHoras({
  id,
  tareaNombre,
  cantidadHoras,
  carga,
  //onEliminar,
  //onModificar
}: {
  id: string;
  tareaNombre: string;
  cantidadHoras: number;
  carga: Partial<CargaDeHoras>;
  // onModificar: (id: string) => void;
  }) {
  const router = useRouter();

  const alturaRem = 6.0 + Math.min(cantidadHoras - 1, 23) * 0.75;

  async function handleEliminarCarga() {
    const confirmar = confirm(
      "¿Estás seguro de que deseas eliminar esta carga?"
    );
    if (!confirmar) return;

    try {
      // Llama a la server action para eliminar la carga de horas
      const resultado = await eliminarCargaHoras(id);

      if (resultado.exito) {
        alert(resultado.mensaje);
        router.refresh();
      } else {
        console.error("Error al eliminar la carga:", resultado.mensaje);
        alert(`Error: ${resultado.mensaje}`);
      }
    } catch (e) {
      console.error("Error inesperado al eliminar la carga:", e);
      alert(`Error inesperado: ${(e as Error).message}`);
    }
  }

  const handleModificarCarga = async (id: string) => {({
    cargasDeHoras: initialCargasDeHoras,
  }: {
    cargasDeHoras: any[];
  }) => {
    const confirmar = confirm("¿Estás seguro de que deseas modificar esta carga?");
    if (!confirmar) return;
    const [cargasDeHoras, setCargasDeHoras] = React.useState(initialCargasDeHoras);
    // router.push(`./pageModificacion/${id}`);
    
    const actualizarCarga = async (id: string, formData: FormData) => {
      try {
        // Llama a la server action para modificar la carga de horas
        const resultado = await modificarCargadeHoras(formData); 
  
        if (resultado.exito) {
          const cargaActualizada = resultado.cargaModificada;

          // Actualiza el estado local para reflejar la modificación
          setCargasDeHoras((prev) => prev.map((carga) => carga.id === id ? {...carga, ...cargaActualizada} : carga));
          alert(resultado.mensaje);
        } else {
          console.error("Error al modificar la carga:", resultado.mensaje);
          alert(`Error: ${resultado.mensaje}`);
        }
      } catch (e) {
        console.error("Error inesperado al modificar la carga:", e);
        alert(`Error inesperado: ${(e as Error).message}`);
      }  
    }
  }

  return (
    <div 
      style={{ height: `${alturaRem}rem` }} 
      className="border border-emerald-500 bg-emerald-200 p-2 overflow-y-auto overflow-x-hidden flex flex-col"
    >
      <span className="font-semibold underline">
        {cantidadHoras} hora{cantidadHoras > 1 && "s"}
      </span>
      <span className="text-sm">{tareaNombre}</span>
      <IconoEliminar onEliminar={() => handleEliminarCarga()} ></IconoEliminar>
      <IconoModificar onModificar={() => handleModificarCarga(id)} ></IconoModificar>
    </div>
  );
  }
}