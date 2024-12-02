"use client"; // Habilita la interactividad en el cliente

// import {useRouter} from "next/navigation";
import { eliminarCargaHoras } from "@/recursos/[recursoId]/cargarHoras"; // Asegúrate de que la importación sea correcta
import { modificarCargadeHoras } from "./modificarCargaDeHoras";
import BloqueCargaDeHoras from "./BloqueCargaDeHoras";
import React from "react";
import { CargaDeHoras } from "@/_lib/tipos";
import { confirmarCargaModificada } from "./verificarCargaDeHorasModificada";

export default function ColumnaCargasDeHoraPorDiaInteractivo({
  cargasDeHoras: initialCargasDeHoras,
}: {
  cargasDeHoras: any[];
}) {
  const [cargasDeHoras, setCargasDeHoras] = React.useState(initialCargasDeHoras);
  // const router = useRouter();

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

  const handleModificarCarga = async (id: string) => {
    const confirmar = confirm("¿Estás seguro de que deseas modificar esta carga?");
    if (!confirmar) return;

    // router.push(`./pageModificacion/${id}`);
    
    const actualizarCarga = async (id: string, formData: FormData) => {
      try {
        // Llama a la server action para modificar la carga de horas
        const resultado = await confirmarCargaModificada(id); 
  
        if (resultado.exito) {
          const cargaActualizada = resultado.cargaModificada;

          // Actualiza el estado local para reflejar la modificación
          setCargasDeHoras((prev) =>
              prev.map((carga) =>
                  carga.id === id ? {...carga, ...cargaActualizada} : carga));
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
    <div className="flex flex-col gap-1.5">
      {cargasDeHoras.map((carga) => (
        <div key={carga.id}>
          <BloqueCargaDeHoras 
              {...carga} 
              onEliminar={handleEliminarCarga} 
              onModificar={handleModificarCarga} 
          />
        </div>
      ))}
    </div>
  );
}
