import type { Proyecto, Recurso } from "@/app/lib/types";

export default async function Home() {
  const dataProyectos = await fetch("http://localhost:8080/proyectos");
  const proyectos: Proyecto[] = await dataProyectos.json();

  const dataRecursos = await fetch("http://localhost:8080/proyectos");
  const recursos: Recurso[] = await dataRecursos.json();

  return <div>Hola mundo</div>;
}
