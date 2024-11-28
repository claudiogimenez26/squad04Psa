import { desencodearFecha } from "@/_lib/fecha";
import { CargaDeHoras } from "@/_lib/tipos";

const INDICES_DIAS = {
  Domingo: 0,
  Lunes: 1,
  Martes: 2,
  Miércoles: 3,
  Jueves: 4,
  Viernes: 5,
  Sábado: 6
};

export default async function ({
  params,
  searchParams
}: {
  params: Promise<{ recursoId: string }>;
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>;
}) {
  const recursoId = (await params).recursoId;
  let fecha = (await searchParams)["fecha"];

  if (fecha && typeof fecha != "string") {
    fecha = fecha[0];
  }

  const url = `${process.env.BACKEND_URL}/carga-de-horas/${recursoId}?fecha=${fecha}`;
  console.log(url);

  const res = await fetch(url);

  let cargas: CargaDeHoras[] = [];

  try {
    cargas = await res.json();
  } catch (e) {
    console.error(e);
  }

  const cargasPorDia = cargas.map((c) => {
    const fechaCarga = desencodearFecha(c.fechaCarga);
    return { ...c, fechaCarga };
  });

  return (
    <table className="border border-gray-300 w-full">
      <thead className="bg-gray-50 border-b border-gray-300">
        <tr className="grid grid-cols-7">
          {Object.keys(INDICES_DIAS).map((d) => (
            <td key={d} className="p-2 text-center">
              {d}
            </td>
          ))}
        </tr>
      </thead>
      <tbody>
        {cargasPorDia.length > 0 ? (
          <tr className="grid grid-cols-7 gap-1 p-1.5">
            {Object.values(INDICES_DIAS).map((i) => (
              <td key={i}>
                <ColumnaCargasDeHoraPorDia
                  cargasDeHoras={cargasPorDia.filter(
                    (c) => c.fechaCarga.getDay() === i
                  )}
                />
              </td>
            ))}
          </tr>
        ) : (
          <tr>
            <td className="text-center p-4">
              El recurso no tiene cargas de horas en esta semana.
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
}

function ColumnaCargasDeHoraPorDia({
  cargasDeHoras
}: {
  cargasDeHoras: (Omit<CargaDeHoras, "fechaCarga"> & {
    fechaCarga: Date;
  })[];
}) {
  return (
    <div className="flex flex-col gap-1.5">
      {cargasDeHoras.map((c) => (
        <div key={c.id}>
          <BloqueCargaDeHoras {...c} />
        </div>
      ))}
    </div>
  );
}

function BloqueCargaDeHoras({
  tareaNombre,
  cantidadHoras
}: {
  tareaNombre: string;
  cantidadHoras: number;
}) {
  const alturaRem = 6.0 + Math.min(cantidadHoras - 1, 23) * 0.75;

  return (
    <div
      className="border border-emerald-500 bg-emerald-200 p-2 overflow-y-auto overflow-x-hidden flex flex-col"
      style={{ height: `${alturaRem}rem` }}
    >
      <span className="font-semibold underline">
        {cantidadHoras} hora{cantidadHoras > 1 && "s"}
      </span>
      <span className="text-sm">{tareaNombre}</span>
    </div>
  );
}
