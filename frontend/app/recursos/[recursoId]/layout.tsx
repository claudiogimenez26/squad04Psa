import Formulario from "./Formulario";

export default async function ({ children }: { children: React.ReactElement }) {
  const data = await fetch(`${process.env.BACKEND_URL}/recursos`);
  const recursos = await data.json();

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Horas por recurso</h1>
      <Formulario recursos={recursos}>{children}</Formulario>
    </div>
  );
}
