import React from "react";

type CostoRecurso = {
  id: string;
  nombre: string;
  rol: string;
  costoRol: number;
  costos: { mes: string; cantidadHoras: number }[];
};

export default async function CostosPorRecurso({
  params,
  searchParams,
}: {
  params: { proyectoId: string };
  searchParams: { fechaInicio?: string; fechaFin?: string };
}) {
  const { proyectoId } = params;
  const { fechaInicio, fechaFin } = searchParams;

  const url = `${process.env.BACKEND_URL}/proyectos/${proyectoId}/recursos?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
  const res = await fetch(url);

  let costosPorRecurso: CostoRecurso[] = [];
  try {
    costosPorRecurso = await res.json();
  } catch (e) {
    console.error(e);
  }

  if (!costosPorRecurso || costosPorRecurso.length === 0) {
    return <div>No hay datos de costos para este proyecto.</div>;
  }

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full table-auto border-collapse border border-gray-300">
        <thead className="bg-gray-100">
          <tr>
            <th className="border border-gray-300 p-2">Recurso</th>
            <th className="border border-gray-300 p-2">Rol</th>
            <th className="border border-gray-300 p-2">Costo por Hora</th>
            <th className="border border-gray-300 p-2">Mes</th>
            <th className="border border-gray-300 p-2">Horas</th>
            <th className="border border-gray-300 p-2">Costo Total</th>
          </tr>
        </thead>
        <tbody>
          {costosPorRecurso.map((recurso) =>
            recurso.costos.map((costo, index) => (
              <tr
                key={`${recurso.id}-${costo.mes}-${index}`}
                className="text-center"
              >
                {index === 0 && (
                  <>
                    <td
                      rowSpan={recurso.costos.length}
                      className="border border-gray-300 p-2 font-semibold"
                    >
                      {recurso.nombre}
                    </td>
                    <td
                      rowSpan={recurso.costos.length}
                      className="border border-gray-300 p-2"
                    >
                      {recurso.rol}
                    </td>
                    <td
                      rowSpan={recurso.costos.length}
                      className="border border-gray-300 p-2"
                    >
                      {recurso.costoRol}
                    </td>
                  </>
                )}
                <td className="border border-gray-300 p-2">
                  {costo.mes[0].toUpperCase() + costo.mes.slice(1)}
                </td>
                <td className="border border-gray-300 p-2">
                  {costo.cantidadHoras}
                </td>
                <td className="border border-gray-300 p-2">
                  {costo.cantidadHoras * recurso.costoRol}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
