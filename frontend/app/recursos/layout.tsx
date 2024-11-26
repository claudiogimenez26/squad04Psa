import Formulario from "./Formulario";

export const experimental_ppr = true;

export default async function ({ children }: { children: React.ReactNode }) {
  const data = await fetch(`${process.env.BACKEND_URL}/recursos`);
  const recursos = await data.json();

  return (
    <>
      <Formulario recursos={recursos} />
      {children}
    </>
  );
}
