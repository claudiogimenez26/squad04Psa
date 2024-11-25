"use server";

export async function cargarHoras(formData: FormData) {
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
}

export async function actualizarCargaDeHoras(formData: FormData) {}