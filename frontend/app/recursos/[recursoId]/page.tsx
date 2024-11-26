import { CargaDeHoras } from "@/_lib/tipos";

type CargaDeHorasConFecha = Omit<CargaDeHoras, "fechaCarga"> & {
  fechaCarga: Date;
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

  if (cargasDeHoras.length === 0) {
    return <div>Recurso no tiene cargas de horas</div>;
  }

  const cargasDeHorasConFechas: CargaDeHorasConFecha[] = cargasDeHoras.map(
    (c) => {
      const [dia, mes, anio] = c.fechaCarga.split("/");
      const fechaCarga = new Date(Number(anio), Number(mes), Number(dia));
      return { ...c, fechaCarga };
    }
  );

  const cargasDeHorasLunes: CargaDeHorasConFecha[] =
    cargasDeHorasConFechas.filter((c) => new Date(c.fechaCarga).getDay() !== 1);

  return (
    <div className="grid grid-cols-7">
      <div>
        <div className="p-2">Domingo</div>
      </div>
      <div className="flex flex-col gap-4 divide-y-2">
        <div className="p-2">Lunes</div>
        {cargasDeHorasLunes.map((c) => (
          <div key={c.id}>
            {c.nombreProyecto} - {c.cantidadHoras} horas
          </div>
        ))}
      </div>
      <div>
        <div className="p-2">Martes</div>
      </div>
      <div>
        <div className="p-2">Miércoles</div>
      </div>
      <div>
        <div className="p-2">Jueves</div>
      </div>
      <div>
        <div className="p-2">Viernes</div>
      </div>
      <div>
        <div className="p-2">Sábado</div>
      </div>
    </div>
  );
}
