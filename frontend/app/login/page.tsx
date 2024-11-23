import type { Recurso } from "@/app/lib/types";

import Select from "./Select";
import { Label } from "flowbite-react";

export default async function () {
  const data = await fetch("http://localhost:8080/recursos");
  const recursos: Recurso[] = await data.json();

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Ingresar al sistema</h1>
      <div className="col-span-4 space-y-2">
        <Label htmlFor="recursos" value="Seleccione un recurso" />
        <Select recursos={recursos} />
      </div>
    </div>
  );
}
