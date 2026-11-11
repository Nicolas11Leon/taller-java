package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

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

	public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
					menuOpciones(telefonoInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
					menuOpciones(telefonoInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
					telefonoInputAdapterCli.historial();
					break;
				case OPCION_BUSCAR:
					System.out.print("Ingrese el número a buscar: ");
					keyboard.nextLine();
					String numBuscar = keyboard.nextLine();
					telefonoInputAdapterCli.obtenerTelefono(numBuscar);
					break;
				case OPCION_CREAR:
					TelefonoModelCli nuevo = pedirDatosTelefono(keyboard, null);
					telefonoInputAdapterCli.crearTelefono(nuevo);
					break;
				case OPCION_EDITAR:
					System.out.print("Ingrese el número del teléfono a editar: ");
					keyboard.nextLine();
					String numEditar = keyboard.nextLine();
					TelefonoModelCli editado = pedirDatosTelefono(keyboard, numEditar);
					telefonoInputAdapterCli.modificarTelefono(numEditar, editado);
					break;
				case OPCION_ELIMINAR:
					System.out.print("Ingrese el número del teléfono a eliminar: ");
					keyboard.nextLine();
					String numEliminar = keyboard.nextLine();
					telefonoInputAdapterCli.eliminarTelefono(numEliminar);
					break;
				case OPCION_CONTAR:
					telefonoInputAdapterCli.contar();
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

	private TelefonoModelCli pedirDatosTelefono(Scanner keyboard, String existingNum) {
		String num = "";
		if (existingNum == null) {
			keyboard.nextLine();
			System.out.print("Ingrese el número de teléfono: ");
			num = keyboard.nextLine();
		} else {
			num = existingNum;
		}
		System.out.print("Ingrese el operador: ");
		String oper = keyboard.nextLine();
		System.out.print("Ingrese la cédula del dueño: ");
		int duenio = keyboard.nextInt();
		return new TelefonoModelCli(num, oper, duenio);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los teléfonos");
		System.out.println(OPCION_BUSCAR + " para buscar teléfono por número");
		System.out.println(OPCION_CREAR + " para crear un nuevo teléfono");
		System.out.println(OPCION_EDITAR + " para editar un teléfono existente");
		System.out.println(OPCION_ELIMINAR + " para eliminar un teléfono");
		System.out.println(OPCION_CONTAR + " para contar total de teléfonos");
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
