"use server";

import { CargaDeHoras } from "@/_lib/tipos";


export async function confirmarCargaModificada(cargaHorasId: string) {
  try {
    const res = await fetch(`${process.env.BACKEND_URL}/carga-de-horas/${cargaHorasId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
    });

    if (!res.ok) {
      const text = await res.text();
      console.error(text);
      throw new Error(text);
    }

    const cargaDeHoras: CargaDeHoras = await res.json();
    console.info(cargaDeHoras);

    return {
      exito: true,
      mensaje: `Carga de horas con ID: ${cargaDeHoras.id} modificada con éxito`,
      cargaModificada: {cargaDeHoras}
    };
  } catch (error) {
    return {
      exito: false,
      mensaje:
        (error as Error).message || "Error interno al modificar la carga de horas",
      cargaModificada: null
    };
  }
}