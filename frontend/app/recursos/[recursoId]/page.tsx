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
  params
}: {
  params: Promise<{ recursoId: string }>;
}) {
  const recursoId = (await params).recursoId;

  const data = await fetch(
    `${process.env.BACKEND_URL}/carga-de-horas/${recursoId}`
  );
  const cargasDeHoras: CargaDeHoras[] = await data.json();

  const cargasDeHorasPorDia = cargasDeHoras.map((c) => {
    const [dia, mes, anio] = c.fechaCarga.split("/");
    const fechaCarga = new Date(Number(anio), Number(mes) - 1, Number(dia));
    return { ...c, fechaCarga };
  });

  return (
    <table className="border border-gray-300">
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
        <tr className="grid grid-cols-7">
          {Object.values(INDICES_DIAS).map((i) => (
            <td key={i}>
              <ColumnaCargasDeHoraPorDia
                cargasDeHoras={cargasDeHorasPorDia.filter(
                  (c) => c.fechaCarga.getDay() === i
                )}
              />
            </td>
          ))}
        </tr>
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
    <div className="flex flex-col gap-2 py-2 h-[600px]">
      {cargasDeHoras.map((c) => (
        <div key={c.id}>
          <BloqueCargaDeHoras {...c} />
        </div>
      ))}
    </div>
  );
}

function BloqueCargaDeHoras({
  nombreProyecto,
  cantidadHoras
}: {
  nombreProyecto: string;
  cantidadHoras: number;
}) {
  const altura = (cantidadHoras / 24) * 576;

  return (
    <div
      className="border border-emerald-500 bg-emerald-200 p-2 overflow-y-scroll"
      style={{ height: `${altura}px`, minHeight: "4.5rem" }}
    >
      {nombreProyecto} - {cantidadHoras} horas
    </div>
  );
}
