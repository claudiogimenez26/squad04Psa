"use client";

import { RecursoActualContext } from "@/_context/recursoActualContext";
import { Button, Label, Select } from "flowbite-react";
import { useContext } from "react";

export default function SelectorRecurso() {
  const recursoActualContext = useContext(RecursoActualContext);

  if (
    !recursoActualContext ||
    !recursoActualContext.state ||
    !recursoActualContext.state.recursos ||
    !recursoActualContext.state.recursoActual
  ) {
    return null;
  }

  const { recursos, recursoActual, cargas } = recursoActualContext.state;
  const dispatch = recursoActualContext.dispatch;

  function handleChange(e: React.ChangeEvent<HTMLSelectElement>) {
    const recursoSeleccionado = recursos.find((r) => r.id === e.target.value);
    if (recursoSeleccionado) {
      dispatch({ payload: recursoSeleccionado });
    }
  }

  return (
    <div className="space-y-6">
      <div className="space-y-2">
        <Label htmlFor="recursoId" value="Seleccione un recurso" />
        <Select
          id="recursoId"
          name="recursoId"
          value={recursoActual.id}
          onChange={handleChange}
        >
          {recursos.map((r) => (
            <option key={r.id} value={r.id}>
              {r.dni} - {r.nombre} {r.apellido}
            </option>
          ))}
        </Select>
      </div>
      <h2 className="text-2xl font-semibold">
        Todas las cargas de horas del recurso
      </h2>
      {cargas[recursoActual.id] != null ? (
        <ul className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {cargas[recursoActual.id].map((c) => (
            <li
              key={c.idCarga}
              className="p-4 border rounded-lg border-slate-300 flex flex-col gap-2"
            >
              <p className="font-medium">{c.nombreTarea}</p>
              <p>
                {c.fechaCarga} - {c.cantidadHoras} horas
              </p>
              <div className="gap-2 justify-self-end mt-auto flex">
                <Button color="success" size="sm" className="flex-1">
                  Modificar
                </Button>
                <Button color="failure" size="sm" className="flex-1">
                  Eliminar
                </Button>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p>Este recurso no tiene horas cargadas</p>
      )}
    </div>
  );
}
