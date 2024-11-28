export default async function ({
  params,
  searchParams
}: {
  params: Promise<{ proyectoId: string }>;
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>;
}) {
  const proyectoId = (await params).proyectoId;
  let fecha = (await searchParams)["fecha"];

  if (fecha && typeof fecha != "string") {
    fecha = fecha[0];
  }

  const url = `${process.env.BACKEND_URL}/proyectos/${proyectoId}/recursos?fecha=${fecha}`;

  return <div></div>;
}
