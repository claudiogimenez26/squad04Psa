import { Spinner } from "flowbite-react";

export default function () {
  return (
    <div className="flex items-center justify-center gap-4 p-4">
      <span>Cargando costos del proyecto...</span>
      <Spinner />
    </div>
  );
}
