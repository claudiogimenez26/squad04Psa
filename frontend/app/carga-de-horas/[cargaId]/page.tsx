export default async function ({
  params
}: {
  params: Promise<{ cargaId: string }>;
}) {
  const cargaId = (await params).cargaId;

  return <div>Hola mundo! {cargaId}</div>;
}
