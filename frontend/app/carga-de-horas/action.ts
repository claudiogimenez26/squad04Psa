"use server";

import { CargaDeHoras } from "@/_lib/tipos";

export async function cargarHoras(formData: FormData) {
  try {
    const res = await fetch(`${process.env.BACKEND_URL}/carga-de-horas`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        tareaId: formData.get("tareaId"),
        recursoId: formData.get("recursoId"),
        cantidadHoras: Number(formData.get("cantidadHoras")),
        fechaCarga: formData.get("fechaCarga")
      })
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
      mensaje: `Carga de horas registrada exitosamente con ID: ${cargaDeHoras.id}`
    };
  } catch (error) {
    return {
      exito: false,
      mensaje:
        (error as Error).message || "Error interno al guardar la carga de horas"
    };
  }
}
