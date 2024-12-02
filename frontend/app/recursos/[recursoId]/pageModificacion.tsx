import { useRouter } from "next/router"; 
import type { Proyecto, Tarea, CargaDeHoras } from "@/_lib/tipos";
import FormularioModificacion from "./FormularioModificacion";

// export default function ModificarCarga () {
//   const router = useRouter();
//   const { cargaModificadaId } = router.query

//   return (
//     <div className="space-y-6">
//       <h1 className="text-4xl font-bold">Modificar carga de horas</h1>
//       <FormularioModificacion proyectoId={cargaModificada.proyectoId} tareaId={cargaModificada.tareaId} />
//     </div>
//   );
// }

export default async function ({ params }: { params: { id: string } }) {
  const cargaId = params.id;

  const [proyectos, tareas, cargas] = (await Promise.all([
    fetch(`${process.env.BACKEND_URL}/proyectos`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/tareas`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/carga-de-horas`).then((res) => res.json()),
  ])) as [Proyecto[], Tarea[], CargaDeHoras[]];

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Modifacar carga de horas</h1>
      <FormularioModificacion 
          proyectos={proyectos} 
          tareas={tareas} 
          cargas={cargas} 
          cargaModificadaId={cargaId}
      />
    </div>
  );
}
