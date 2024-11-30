import { desencodearFecha } from "@/_lib/fecha";
import { CargaDeHoras } from "@/_lib/tipos";
import ColumnaCargasDeHoraPorDiaInteractivo from "./ColumnaCargasDeHoraPorDiaInteractivo";

const INDICES_DIAS = {
  Domingo: 0,
  Lunes: 1,
  Martes: 2,
  Miércoles: 3,
  Jueves: 4,
  Viernes: 5,
  Sábado: 6
};

export default async function Page({
  params,
  searchParams
}: {
  params: Promise<{ recursoId: string }>;
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>;
}) {
  const recursoId = (await params).recursoId;
  let fecha = (await searchParams)["fecha"];

  if (fecha && typeof fecha !== "string") {
    fecha = fecha[0];
  }

  const url = `${process.env.BACKEND_URL}/carga-de-horas/${recursoId}?fecha=${fecha}`;
  const res = await fetch(url);

  let cargas = [];

  try {
    cargas = await res.json();
  } catch (e) {
    console.error(e);
  }

  const cargasPorDia = cargas.map((c: any) => {
    const fechaCarga = desencodearFecha(c.fechaCarga);
    return { ...c, fechaCarga };
  });

  return (
    <div>
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
          <tr className="grid grid-cols-7 gap-1 p-1.5">
            {Object.values(INDICES_DIAS).map((i) => (
              <td key={i}>
                <ColumnaCargasDeHoraPorDiaInteractivo
                  cargasDeHoras={cargasPorDia.filter(
                    (c: any) => c.fechaCarga.getDay() === i
                  )}
                />
              </td>
            ))}
          </tr>
        </tbody>
      </table>
    </div>
  );
}


