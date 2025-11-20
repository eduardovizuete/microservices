# Monorepo con ramas de proyectos independientes y preservando historial

## Objetivo

- Un solo repositorio en GitHub.
- Rama `main` con solo un README.md.
- Ramas de proyecto (`codearti`, etc.) con carpetas de proyectos completos, cada uno con su historial de commits preservado.
- Sin submódulos ni referencias a otros repositorios.

---

## 1. Crear el repositorio principal

```sh
git init microservices
cd microservices
echo "# Microservices" > README.md
git add README.md
git commit -m "init: add README"
git branch -M main
git remote add origin git@github.com:TU_USUARIO/microservices.git
git push -u origin main
```

---

## 2. Importar un proyecto existente a una rama nueva

Supón que tienes el proyecto `arquitectura_micro_springboot_kubernetes` en otra carpeta fuera del repo principal.

### a) Clona el repo principal en otra carpeta (opcional, pero recomendado para evitar conflictos):

```sh
git clone git@github.com:TU_USUARIO/microservices.git
cd microservices
```

### b) Crea una rama para el proyecto:

```sh
git checkout -b codearti
```

### c) Importa el proyecto preservando su historial

Supón que el proyecto está en `/ruta/al/proyecto/arquitectura_micro_springboot_kubernetes`:

```sh
# Desde la raíz del repo microservices
git remote add arqui /ruta/al/proyecto/arquitectura_micro_springboot_kubernetes
git fetch arqui
git merge --allow-unrelated-histories --no-commit arqui/main
mkdir -p codearti
# Mueve todo el contenido importado a la carpeta codearti/
find . -maxdepth 1 ! -name .git ! -name codearti ! -name . -exec mv {} codearti/ \;
git add .
git commit -m "import: add arquitectura_micro_springboot_kubernetes to codearti/"
git remote remove arqui
```

> Si el proyecto tiene otra rama principal (por ejemplo, master), reemplaza `arqui/main` por `arqui/master`.

### d) Empuja la rama al remoto

```sh
git push -u origin codearti
```

---

## 3. Repite para otros proyectos

Crea nuevas ramas y repite el proceso para cada proyecto que quieras importar.

---

## 4. Notas

- No uses submódulos ni subtrees si no quieres dependencias externas.
- Cada rama puede tener su propio proyecto en una carpeta, con historial preservado.
- El branch `main` permanece limpio con solo el README.md.
- Puedes hacer PRs entre ramas si necesitas fusionar proyectos.

---

## 5. Comandos usados

- `git remote add` y `git fetch`: para traer el historial del proyecto externo.
- `git merge --allow-unrelated-histories`: para fusionar historiales distintos.
- `find ... -exec mv ...`: para mover los archivos importados a la carpeta deseada.
- `git commit`: para guardar los cambios.
- `git push`: para subir la rama al remoto.

---

## 6. Referencias

- [Git: Import a repository into a subdirectory](https://stackoverflow.com/a/10548933)
- [GitHub Docs: Merging unrelated histories](https://docs.github.com/en/get-started/using-git/using-git#merging-unrelated-histories)
