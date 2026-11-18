import os

entities = [
    ("Persona", "PersonUseCase", "cc", "Person", "persona", [
        ("findPhones", "Consultar Teléfonos de una Persona", "cc", "List&lt;Phone&gt;"),
        ("findStudies", "Consultar Estudios de una Persona", "cc", "List&lt;Study&gt;"),
    ]),
    ("Profesión", "ProfessionUseCase", "id", "Profession", "profesion", [
        ("findStudies", "Consultar Estudios de una Profesión", "id", "List&lt;Study&gt;"),
    ]),
    ("Teléfono", "PhoneUseCase", "num", "Phone", "telefono", [
        ("findOwner", "Consultar Dueño de un Teléfono", "num", "Person"),
    ]),
    ("Estudios", "StudyUseCase", "idProf, ccPer", "Study", "estudio", [
        ("findPerson", "Consultar Persona de un Estudio", "idProf, ccPer", "Person"),
        ("findProfession", "Consultar Profesión de un Estudio", "idProf, ccPer", "Profession"),
    ]),
]

def card_flow(num, title, entry, step1, decision, yes_step, no_step):
    return f"""
    <div class="flow-card">
      <div class="flow-title">{num}. {title}</div>
      <div class="flow-steps">
        <span class="box start">{entry}</span>
        <span class="arrow">&rarr;</span>
        <span class="box proc">Inyectar Adapter (MARIA/MONGO)</span>
        <span class="arrow">&rarr;</span>
        <span class="box proc">{step1}</span>
        <span class="arrow">&rarr;</span>
        <span class="box dec">{decision}</span>
        <span class="arrow">&rarr;</span>
        <span class="branch">
          <span class="ok">Sí: {yes_step}</span><br>
          <span class="err">No: {no_step}</span>
        </span>
      </div>
    </div>
    """

cards_html = []
idx = 1
for ent_name, uc, pk, dom, path, rels in entities:
    cards_html.append(f"<h2>Módulo {ent_name} (<code>{uc}</code>)</h2>")
    cards_html.append(card_flow(idx, f"{ent_name} — findAll()", f"GET /api/v1/{path}/{{db}}", f"{uc}.findAll()", "¿BD válida?", f"200 OK List&lt;{dom}&gt;", "400 InvalidOption")); idx += 1
    cards_html.append(card_flow(idx, f"{ent_name} — findOne({pk})", f"GET /api/v1/{path}/{{db}}/{{{pk}}}", f"findById({pk})", "¿Existe?", f"200 OK {dom}", "404 NoexistException")); idx += 1
    cards_html.append(card_flow(idx, f"{ent_name} — count()", f"GET /api/v1/{path}/{{db}}/count", f"{uc}.count()", "¿BD válida?", "200 OK Integer", "400 InvalidOption")); idx += 1
    cards_html.append(card_flow(idx, f"{ent_name} — create({dom})", f"POST /api/v1/{path}", f"outputPort.save({dom})", "¿Payload @Valid?", f"200/201 {dom}", "400 Bad Request")); idx += 1
    cards_html.append(card_flow(idx, f"{ent_name} — edit({pk})", f"PUT /api/v1/{path}/{{db}}/{{{pk}}}", f"findById({pk}) &rarr; save()", "¿Existe?", f"200 OK {dom}", "404 NoexistException")); idx += 1
    cards_html.append(card_flow(idx, f"{ent_name} — drop({pk})", f"DELETE /api/v1/{path}/{{db}}/{{{pk}}}", f"findById({pk}) &rarr; delete()", "¿Existe?", "200 OK true", "404 NoexistException")); idx += 1
    for rm, rt, rpk, rret in rels:
        cards_html.append(card_flow(idx, f"{ent_name} — {rm}({rpk})", f"GET relación {rm}", f"findById({rpk})", "¿Existe?", f"200 OK {rret}", "404 NoexistException")); idx += 1

html = f"""<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Diagramas de Flujo por Caso de Uso - PersonAPP</title>
<style>
  @page {{ size: letter; margin: 18mm 14mm; }}
  body {{ font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #1e293b; font-size: 9.5pt; line-height: 1.4; }}
  .cover {{ text-align: center; padding-top: 20px; }}
  .cover-header {{ border-bottom: 3px solid #1e3a8a; padding-bottom: 12px; margin-bottom: 25px; }}
  .cover-header h1 {{ font-size: 18pt; color: #1e3a8a; margin: 0 0 6px 0; text-transform: uppercase; }}
  .cover-header h2 {{ font-size: 11.5pt; color: #475569; font-weight: normal; margin: 0; }}
  .cover-title {{ margin: 35px 0; }}
  .cover-title h3 {{ font-size: 19pt; color: #0f172a; margin: 0 0 10px 0; }}
  .cover-title .subtitle {{ font-size: 11.5pt; color: #2563eb; font-weight: 600; }}
  .cover-info {{ margin-top: 35px; font-size: 10pt; line-height: 1.7; }}
  .cover-footer {{ margin-top: 45px; font-size: 9pt; color: #64748b; border-top: 1px solid #e2e8f0; padding-top: 12px; }}
  .page-break {{ page-break-before: always; }}
  h1 {{ color: #1e3a8a; font-size: 15pt; border-bottom: 2px solid #e2e8f0; padding-bottom: 5px; margin-top: 18px; }}
  h2 {{ color: #0f172a; font-size: 12pt; margin-top: 18px; margin-bottom: 8px; border-left: 4px solid #2563eb; padding-left: 8px; }}
  .flow-card {{ border: 1px solid #cbd5e1; border-radius: 6px; padding: 8px 10px; margin-bottom: 8px; background: #f8fafc; page-break-inside: avoid; }}
  .flow-title {{ font-weight: bold; color: #1e3a8a; font-size: 9.5pt; margin-bottom: 6px; }}
  .flow-steps {{ font-size: 8pt; line-height: 1.8; }}
  .box {{ display: inline-block; padding: 2px 6px; border-radius: 4px; border: 1px solid #94a3b8; background: #ffffff; }}
  .start {{ background: #eff6ff; border-color: #3b82f6; color: #1d4ed8; font-weight: bold; }}
  .proc {{ background: #ffffff; border-color: #64748b; color: #0f172a; }}
  .dec {{ background: #fef3c7; border-color: #d97706; color: #92400e; font-weight: bold; }}
  .arrow {{ color: #2563eb; font-weight: bold; margin: 0 3px; }}
  .branch {{ display: inline-block; vertical-align: middle; font-size: 7.5pt; }}
  .ok {{ color: #047857; font-weight: bold; }}
  .err {{ color: #b91c1c; font-weight: bold; }}
</style>
</head>
<body>
<div class="cover">
  <div class="cover-header">
    <h1>Pontificia Universidad Javeriana</h1>
    <h2>Facultad de Ingeniería &bull; Departamento de Ingeniería de Sistemas</h2>
    <h2>Asignatura: Arquitectura de Software</h2>
  </div>
  <div class="cover-title">
    <h3>Catálogo Completo de Diagramas de Flujo por Caso de Uso</h3>
    <div class="subtitle">Laboratorio 2: PersonAPP — Arquitectura Hexagonal con Repository y Service</div>
  </div>
  <div class="cover-info">
    <p><strong>Integrantes del Equipo:</strong><br>
    Nicolás León Parra (<code>Nicolas11Leon</code>)<br>
    Juan Pablo Espinosa (<code>savaged-22</code>)<br>
    Equipo de Trabajo (<code>Warriorr89</code>)</p>
    <p><strong>Docente:</strong> Andrés Sánchez</p>
    <p><strong>Fecha:</strong> Noviembre de 2026</p>
  </div>
  <div class="cover-footer">
    Repositorio: https://github.com/Nicolas11Leon/taller-java &bull; Release: v1.0.0<br>
    Bogotá D.C., Colombia &bull; 2026
  </div>
</div>

<div class="page-break"></div>

<h1>1. Flujos de Ejecución por Caso de Uso (30 Operaciones)</h1>
<p>Cada uno de los 30 flujos muestra el recorrido completo desde la entrada (REST/CLI), la inyección dinámica del adaptador de persistencia (<code>MARIA</code> o <code>MONGO</code>), la ejecución en el caso de uso y la respuesta HTTP/DTO correspondiente.</p>

{''.join(cards_html)}

</body>
</html>
"""

with open("/tmp/diagramas_flujo.html", "w") as f:
    f.write(html)
print("HTML de diagramas de flujo generado exitosamente.")
