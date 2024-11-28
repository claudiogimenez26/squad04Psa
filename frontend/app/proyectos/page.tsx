import { encodearFecha } from "@/_lib/fecha";
import { redirect } from "next/navigation";

export default async function () {
  const res = await fetch(`${process.env.BACKEND_URL}/proyectos`);
  const proyectos = await res.json();

  const fechaFin = encodearFecha(new Date());
  const unAnioAtras = new Date();
  unAnioAtras.setFullYear(unAnioAtras.getFullYear() - 1);
  const fechaInicio = encodearFecha(unAnioAtras);

  redirect(
    `/proyectos/${proyectos[0].id}?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`
  );
}
