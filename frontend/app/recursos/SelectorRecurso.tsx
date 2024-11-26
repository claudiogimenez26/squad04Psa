"use client";

import { Recurso } from "@/_lib/tipos";
import { Label, Select } from "flowbite-react";
import { useRouter } from "next/navigation";

export default function SelectorRecurso({ recursos }: { recursos: Recurso[] }) {
  const router = useRouter();

  function handleChange(e: React.ChangeEvent<HTMLSelectElement>) {
    const recursoSeleccionado = recursos.find((r) => r.id === e.target.value);
    if (recursoSeleccionado) {
      router.push(`/recursos/${recursoSeleccionado.id}`);
    }
  }

  return (
    <div className="space-y-2">
      <Label htmlFor="recursoId" value="Seleccione un recurso" />
      <Select id="recursoId" onChange={handleChange}>
        {recursos.map((r) => (
          <option key={r.id} value={r.id}>
            {r.dni} - {r.nombre} {r.apellido}
          </option>
        ))}
      </Select>
    </div>
  );
}
