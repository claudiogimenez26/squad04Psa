"use server";

import { z } from "zod";

const schema = z.object({
  tareaId: z.string().uuid(),
  recursoId: z.string().uuid(),
  cantidadHoras: z.number().gt(0).lte(24),
  fechaDeCarga: z.string().date(),
});

export async function cargarHoras(formData: FormData) {
  const data = schema.parse({
    tareaId: formData.get("tareaId"),
    recursoId: formData.get("recursoId"),
    cantidadHoras: formData.get("cantidadHoras"),
    fechaDeCarga: formData.get("fechaDeCarga"),
  });
}
