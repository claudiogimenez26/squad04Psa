import { Proyecto } from "@/_lib/tipos";
import Formulario from "./Formulario";

export default async function ({ children }: { children: React.ReactNode }) {
  const res = await fetch(`${process.env.BACKEND_URL}/proyectos`);
  const proyectos: Proyecto[] = await res.json();

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Costos por proyecto</h1>
      <Formulario proyectos={proyectos}>{children}</Formulario>
    </div>
  );
}
