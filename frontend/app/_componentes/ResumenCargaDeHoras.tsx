import { Proyecto, Tarea } from "@/_lib/tipos";
import { Table } from "flowbite-react";

export default function ResumenCargaDeHoras({
  proyecto,
  tarea,
  cantidadHoras,
  fechaCarga
}: {
  proyecto: Proyecto;
  tarea: Tarea;
  cantidadHoras: number;
  fechaCarga: Date;
}) {
  return (
    <Table>
      <Table.Body className="divide-y">
        <Table.Row>
          <Table.Cell>Proyecto</Table.Cell>
          <Table.Cell>
            {proyecto.id} - {proyecto.nombre}
          </Table.Cell>
        </Table.Row>
        <Table.Row>
          <Table.Cell>Tarea</Table.Cell>
          <Table.Cell>
            {tarea.id} - {tarea.nombre}
          </Table.Cell>
        </Table.Row>
        <Table.Row>
          <Table.Cell>Cantidad de horas</Table.Cell>
          <Table.Cell>{cantidadHoras}</Table.Cell>
        </Table.Row>
        <Table.Row>
          <Table.Cell>Fecha de carga</Table.Cell>
          <Table.Cell>{fechaCarga.toLocaleDateString("es-AR")}</Table.Cell>
        </Table.Row>
      </Table.Body>
    </Table>
  );
}
