import SelectorRecurso from "@/_componentes/SelectorRecurso";
import type { Proyecto, Tarea } from "@/_lib/tipos";
import Formulario from "./Formulario";

export default async function () {
  const [proyectos, tareas] = (await Promise.all([
    fetch(`${process.env.BACKEND_URL}/proyectos`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/tareas`).then((res) => res.json())
  ])) as [Proyecto[], Tarea[]];

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Cargar horas</h1>
      <SelectorRecurso />
      <Formulario proyectos={proyectos} tareas={tareas} />
    </div>
  );
}
