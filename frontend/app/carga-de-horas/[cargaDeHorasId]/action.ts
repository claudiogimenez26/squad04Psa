"use server";

import { CargaDeHoras } from "@/_lib/tipos";

export async function modificarCargaDeHoras(formData: FormData) {
  try {
    const body = JSON.stringify({
      tareaId: formData.get("tareaId"),
      recursoId: formData.get("recursoId"),
      cantidadHoras: Number(formData.get("cantidadHoras")),
      fechaCarga: formData.get("fechaCarga")
    });

    console.log(body);

    const res = await fetch(
      `${process.env.BACKEND_URL}/carga-de-horas/${formData.get("cargaId")}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body
      }
    );

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
      cargaModificada: { cargaDeHoras }
    };
  } catch (error) {
    return {
      exito: false,
      mensaje:
        (error as Error).message ||
        "Error interno al modificar la carga de horas",
      cargaModificada: null
    };
  }
}
