"use client"; // También es un componente interactivo

export default function BloqueCargaDeHoras({
  id,
  tareaNombre,
  cantidadHoras,
  onEliminar
}: {
  id: string;
  tareaNombre: string;
  cantidadHoras: number;
  onEliminar: (id: string) => void;
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
      <button
        onClick={() => onEliminar(id)} // Llama al manejador
        className="text-red-600 font-bold mt-2 hover:underline"
      >
        Eliminar
      </button>
    </div>
  );
}
