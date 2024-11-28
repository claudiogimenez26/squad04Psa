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

  let costosPorProyecto: CostosDeRecursosPorProyecto | null = null;

  try {
    costosPorProyecto = await res.json();
  } catch (e) {
    console.error(e);
  }

  if (!costosPorProyecto) {
    return null;
  }

  return (
    <div>
      {Object.keys(costosPorProyecto).map((recursoId) => (
        <div key={recursoId}>
          {costosPorProyecto[recursoId].map((mes) => (
            <div key={`${recursoId}-${mes.mes}`}>
              <p>Recurso: {mes.nombreRecurso}</p>
              <p>Rol: {mes.nombreRol}</p>
              <p>Horas: {mes.horasTrabajadas}</p>
            </div>
          ))}
        </div>
      ))}
    </div>
  );
}
