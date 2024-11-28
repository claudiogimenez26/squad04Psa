import { CostosDeRecursosPorProyecto } from "@/_lib/tipos";

export default async function ({
  params,
  searchParams
}: {
  params: Promise<{ proyectoId: string }>;
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>;
}) {
  const proyectoId = (await params).proyectoId;
  const searchParamsDict = await searchParams;

  let fechaInicio = searchParamsDict["fechaInicio"];
  let fechaFin = searchParamsDict["fechaFin"];

  if (
    fechaInicio &&
    fechaFin &&
    typeof fechaInicio != "string" &&
    typeof fechaFin != "string"
  ) {
    fechaInicio = fechaInicio[0];
    fechaFin = fechaFin[0];
  }

  const url = `${process.env.BACKEND_URL}/proyectos/${proyectoId}/recursos?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
  const res = await fetch(url);

  let costosPorRecurso: CostosDeRecursosPorProyecto[] = [];

  try {
    costosPorRecurso = await res.json();
  } catch (e) {
    console.error(e);
  }

  if (!costosPorRecurso) {
    return null;
  }

  if (costosPorRecurso.length === 0) {
    return <div>Proyecto no tiene horas cargas en este rango de fechas</div>;
  }

  return (
    <div className="space-y-6">
      {costosPorRecurso.map((r) => (
        <div key={r.id} className="space-y-3">
          <h2 className="text-xl font-semibold">{r.nombre}</h2>
          <div className="space-y-1">
            <p>
              {r.rol} (Costo p/hora: {r.costoRol})
            </p>
            <p>
              Costo total en el período:{" "}
              {r.costos.reduce((acc, curr) => acc + curr.cantidadHoras, 0) *
                r.costoRol}
            </p>
          </div>
          <ul key={r.id} className="space-y-4">
            {r.costos.map((mes) => (
              <li
                key={`${r.id}-${mes.mes}`}
                className="p-4 border rounded-lg border-gray-300"
              >
                <p>Mes: {mes.mes[0].toUpperCase() + mes.mes.slice(1)}</p>
                <p>Horas: {mes.cantidadHoras}</p>
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}
