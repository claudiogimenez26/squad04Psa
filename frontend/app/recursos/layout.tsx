import SelectorFecha from "./SelectorFecha";
import SelectorRecurso from "./SelectorRecurso";

export const experimental_ppr = true;

export default async function ({ children }: { children: React.ReactNode }) {
  const data = await fetch(`${process.env.BACKEND_URL}/recursos`);
  const recursos = await data.json();

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Consultar cargas por recurso</h1>

      <div className="space-y-4">
        <SelectorRecurso recursos={recursos} />
        <SelectorFecha />
      </div>

      {children}
    </div>
  );
}
