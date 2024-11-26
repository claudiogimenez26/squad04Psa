import SelectRecurso from "@/_componentes/SelectRecurso";

export default async function () {
  return (
    <div className="flex-1 space-y-6">
      <h1 className="text-4xl font-bold">Cambiar recurso</h1>
      <SelectRecurso />
    </div>
  );
}
