package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_BUSCAR = 2;
	private static final int OPCION_CREAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;
	private static final int OPCION_CONTAR = 6;

	public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MODULOS:
					isValid = true;
					break;
				case PERSISTENCIA_MARIADB:
					profesionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
					menuOpciones(profesionInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					profesionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
					menuOpciones(profesionInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR:
					isValid = true;
					break;
				case OPCION_VER_TODO:
					profesionInputAdapterCli.historial();
					break;
				case OPCION_BUSCAR:
					System.out.print("Ingrese el ID de la profesión a buscar: ");
					int idBuscar = keyboard.nextInt();
					profesionInputAdapterCli.obtenerProfesion(idBuscar);
					break;
				case OPCION_CREAR:
					ProfesionModelCli nueva = pedirDatosProfesion(keyboard, null);
					profesionInputAdapterCli.crearProfesion(nueva);
					break;
				case OPCION_EDITAR:
					System.out.print("Ingrese el ID de la profesión a editar: ");
					int idEditar = keyboard.nextInt();
					ProfesionModelCli editada = pedirDatosProfesion(keyboard, idEditar);
					profesionInputAdapterCli.modificarProfesion(idEditar, editada);
					break;
				case OPCION_ELIMINAR:
					System.out.print("Ingrese el ID de la profesión a eliminar: ");
					int idEliminar = keyboard.nextInt();
					profesionInputAdapterCli.eliminarProfesion(idEliminar);
					break;
				case OPCION_CONTAR:
					profesionInputAdapterCli.contar();
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
				keyboard.nextLine();
			}
		} while (!isValid);
	}

	private ProfesionModelCli pedirDatosProfesion(Scanner keyboard, Integer existingId) {
		keyboard.nextLine(); // limpiar buffer
		int id = 0;
		if (existingId == null) {
			System.out.print("Ingrese el ID de la profesión: ");
			id = keyboard.nextInt();
			keyboard.nextLine();
		} else {
			id = existingId;
		}
		System.out.print("Ingrese el nombre de la profesión: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese la descripción: ");
		String descripcion = keyboard.nextLine();
		return new ProfesionModelCli(id, nombre, descripcion);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
		System.out.println(OPCION_BUSCAR + " para buscar profesión por ID");
		System.out.println(OPCION_CREAR + " para crear una nueva profesión");
		System.out.println(OPCION_EDITAR + " para editar una profesión existente");
		System.out.println(OPCION_ELIMINAR + " para eliminar una profesión");
		System.out.println(OPCION_CONTAR + " para contar total de profesiones");
		System.out.println(OPCION_REGRESAR + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opción: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
			keyboard.nextLine();
			return leerOpcion(keyboard);
		}
	}
}
