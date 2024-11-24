import SelectRecurso from "@/_componentes/SelectRecurso";
import { Recurso } from "@/_lib/tipos";

export default async function () {
  const res = await fetch(`${process.env.BACKEND_URL}/recursos`);
  const recursos: Recurso[] = await res.json();

  return <SelectRecurso recursos={recursos} />;
}
