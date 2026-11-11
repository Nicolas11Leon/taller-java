package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudiosInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudiosMenu {

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

	public void iniciarMenu(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
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
					estudiosInputAdapterCli.setStudyOutputPortInjection("MARIA");
					menuOpciones(estudiosInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					estudiosInputAdapterCli.setStudyOutputPortInjection("MONGO");
					menuOpciones(estudiosInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
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
					estudiosInputAdapterCli.historial();
					break;
				case OPCION_BUSCAR:
					System.out.print("Ingrese el ID de la profesión: ");
					int idProfBuscar = keyboard.nextInt();
					System.out.print("Ingrese la cédula de la persona: ");
					int ccPerBuscar = keyboard.nextInt();
					estudiosInputAdapterCli.obtenerEstudio(idProfBuscar, ccPerBuscar);
					break;
				case OPCION_CREAR:
					EstudiosModelCli nuevo = pedirDatosEstudio(keyboard, null, null);
					estudiosInputAdapterCli.crearEstudio(nuevo);
					break;
				case OPCION_EDITAR:
					System.out.print("Ingrese el ID de la profesión del estudio a editar: ");
					int idProfEditar = keyboard.nextInt();
					System.out.print("Ingrese la cédula de la persona del estudio a editar: ");
					int ccPerEditar = keyboard.nextInt();
					EstudiosModelCli editado = pedirDatosEstudio(keyboard, idProfEditar, ccPerEditar);
					estudiosInputAdapterCli.modificarEstudio(idProfEditar, ccPerEditar, editado);
					break;
				case OPCION_ELIMINAR:
					System.out.print("Ingrese el ID de la profesión del estudio a eliminar: ");
					int idProfEliminar = keyboard.nextInt();
					System.out.print("Ingrese la cédula de la persona del estudio a eliminar: ");
					int ccPerEliminar = keyboard.nextInt();
					estudiosInputAdapterCli.eliminarEstudio(idProfEliminar, ccPerEliminar);
					break;
				case OPCION_CONTAR:
					estudiosInputAdapterCli.contar();
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

	private EstudiosModelCli pedirDatosEstudio(Scanner keyboard, Integer existingIdProf, Integer existingCcPer) {
		int idProf = 0;
		if (existingIdProf == null) {
			System.out.print("Ingrese el ID de la profesión: ");
			idProf = keyboard.nextInt();
		} else {
			idProf = existingIdProf;
		}
		int ccPer = 0;
		if (existingCcPer == null) {
			System.out.print("Ingrese la cédula de la persona: ");
			ccPer = keyboard.nextInt();
			keyboard.nextLine();
		} else {
			ccPer = existingCcPer;
			keyboard.nextLine();
		}
		System.out.print("Ingrese la fecha de graduación (YYYY-MM-DD): ");
		String fecha = keyboard.nextLine();
		System.out.print("Ingrese el nombre de la universidad: ");
		String univer = keyboard.nextLine();
		return new EstudiosModelCli(idProf, ccPer, fecha, univer);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
		System.out.println(OPCION_BUSCAR + " para buscar estudio por profesión y persona");
		System.out.println(OPCION_CREAR + " para crear un nuevo estudio");
		System.out.println(OPCION_EDITAR + " para editar un estudio existente");
		System.out.println(OPCION_ELIMINAR + " para eliminar un estudio");
		System.out.println(OPCION_CONTAR + " para contar total de estudios");
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
