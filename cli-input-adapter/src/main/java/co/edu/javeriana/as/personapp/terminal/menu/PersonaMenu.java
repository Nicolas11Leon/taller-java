package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

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

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
					personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
					menuOpciones(personaInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
					menuOpciones(personaInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
					personaInputAdapterCli.historial();
					break;
				case OPCION_BUSCAR:
					System.out.print("Ingrese la cédula a buscar: ");
					int ccBuscar = keyboard.nextInt();
					personaInputAdapterCli.obtenerPersona(ccBuscar);
					break;
				case OPCION_CREAR:
					PersonaModelCli nueva = pedirDatosPersona(keyboard, null);
					personaInputAdapterCli.crearPersona(nueva);
					break;
				case OPCION_EDITAR:
					System.out.print("Ingrese la cédula de la persona a editar: ");
					int ccEditar = keyboard.nextInt();
					PersonaModelCli editada = pedirDatosPersona(keyboard, ccEditar);
					personaInputAdapterCli.modificarPersona(ccEditar, editada);
					break;
				case OPCION_ELIMINAR:
					System.out.print("Ingrese la cédula de la persona a eliminar: ");
					int ccEliminar = keyboard.nextInt();
					personaInputAdapterCli.eliminarPersona(ccEliminar);
					break;
				case OPCION_CONTAR:
					personaInputAdapterCli.contar();
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

	private PersonaModelCli pedirDatosPersona(Scanner keyboard, Integer existingCc) {
		keyboard.nextLine(); // limpiar buffer
		int cc = 0;
		if (existingCc == null) {
			System.out.print("Ingrese la cédula: ");
			cc = keyboard.nextInt();
			keyboard.nextLine();
		} else {
			cc = existingCc;
		}
		System.out.print("Ingrese el nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese el apellido: ");
		String apellido = keyboard.nextLine();
		System.out.print("Ingrese el género (M/F): ");
		String genero = keyboard.nextLine();
		System.out.print("Ingrese la edad: ");
		int edad = keyboard.nextInt();
		return new PersonaModelCli(cc, nombre, apellido, genero, edad);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_BUSCAR + " para buscar persona por cédula");
		System.out.println(OPCION_CREAR + " para crear una nueva persona");
		System.out.println(OPCION_EDITAR + " para editar una persona existente");
		System.out.println(OPCION_ELIMINAR + " para eliminar una persona");
		System.out.println(OPCION_CONTAR + " para contar total de personas");
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
