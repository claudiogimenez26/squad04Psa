import FormularioConsultaCostos from "@/_componentes/FormularioConsultaCostos";
import { Proyecto, Recurso } from "@/_lib/tipos";

export default async function () {
  const [proyectos, recursos] = (await Promise.all([
    fetch(`${process.env.BACKEND_URL}/proyectos`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/recursos`).then((res) => res.json())
  ])) as [Proyecto[], Recurso[]];

  return <FormularioConsultaCostos proyectos={proyectos} recursos={recursos} />;
}
