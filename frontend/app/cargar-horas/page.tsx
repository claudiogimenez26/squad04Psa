import type { Proyecto, Tarea } from "@/_lib/tipos";

import FormularioCargaDeHoras from "@/_componentes/FormularioCargaDeHoras";

export default async function () {
  const [proyectos, tareas] = (await Promise.all([
    fetch(`${process.env.BACKEND_URL}/proyectos`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/tareas`).then((res) => res.json())
  ])) as [Proyecto[], Tarea[]];

  return (
    <div className="flex-1 space-y-6">
      <h1 className="text-4xl font-bold">Cargar horas</h1>
      <FormularioCargaDeHoras proyectos={proyectos} tareas={tareas} />
    </div>
  );
}
