"use server";

import { CargaDeHoras } from "@/_lib/tipos";

export async function eliminarCargaHoras(cargaHorasId: string) {
  try {
    const res = await fetch(`${process.env.BACKEND_URL}/carga-de-horas/${cargaHorasId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!res.ok) {
      const text = await res.text();
      console.error(`Error al eliminar la carga de horas: ${text}`);
      throw new Error(text);
    }

    console.info(`Carga de horas con ID ${cargaHorasId} eliminada correctamente.`);

    return {
      exito: true,
      mensaje: `Carga de horas con ID ${cargaHorasId} eliminada exitosamente.`,
    };
  } catch (error) {
    return {
      exito: false,
      mensaje:
        (error as Error).message || `Error interno al intentar eliminar la carga de horas con ID ${cargaHorasId}.`,
    };
  }
}
