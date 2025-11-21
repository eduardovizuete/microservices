#!/usr/bin/env bash
set -euo pipefail

# Configuration - adjust if needed
ROOT="/Users/eduardo/Documents/microservices"
NESTED_REL="codearti/arquitectura_micro_springboot_kubernetes"   # path inside ROOT where nested repo lives
NESTED_PATH="$ROOT/$NESTED_REL"
TARGET_BRANCH="codearti"
BACKUP_BRANCH="backup/before-add-submodule-$(date +%Y%m%d%H%M%S)"
TMP_REMOTE_NAME="nested-local-temp"

# Safety checks
echo "== Safe submodule convert: add $NESTED_REL as submodule in $ROOT =="
command -v git >/dev/null 2>&1 || { echo "git not found in PATH; install git and re-run"; exit 1; }
[ -d "$ROOT" ] || { echo "Root path $ROOT does not exist"; exit 1; }
[ -d "$NESTED_PATH" ] || { echo "Nested path $NESTED_PATH does not exist"; exit 1; }
[ -d "$NESTED_PATH/.git" ] || { echo "Nested path does not appear to be a git repo (no .git)"; exit 1; }

# Show nested repo info
echo
echo "Nested repo info:"
git -C "$NESTED_PATH" rev-parse --abbrev-ref HEAD || true
git -C "$NESTED_PATH" log --oneline -n 5 || true
git -C "$NESTED_PATH" remote -v || echo "(no remote)"

echo
read -r -p "Proceed to add $NESTED_REL as a submodule on branch $TARGET_BRANCH? (yes/no) " confirm
if [ "$confirm" != "yes" ]; then
  echo "Aborting by user request."
  exit 0
fi

# Switch to repo root and prepare
cd "$ROOT"
echo
echo "Checking out target branch '$TARGET_BRANCH'..."
git fetch origin || true
git checkout "$TARGET_BRANCH"

echo
echo "Creating backup branch '$BACKUP_BRANCH'..."
git branch -f "$BACKUP_BRANCH"   # force-create backup from current HEAD
git push -u origin "$BACKUP_BRANCH" || echo "(push of backup may require network/auth)"

# If outer repo already has a gitlink or accidental entry for the path, remove it from index
if git ls-files --error-unmatch "$NESTED_REL" >/dev/null 2>&1; then
  echo "Removing existing index entry for $NESTED_REL (will keep working tree)..."
  git rm --cached -r "$NESTED_REL"
  git commit -m "chore: remove accidental gitlink for $NESTED_REL before adding submodule" || true
fi

# Add submodule (use the existing local repo path as URL)
echo
echo "Adding submodule (using local path) ..."
git submodule add "$NESTED_PATH" "$NESTED_REL"

# Stage and commit .gitmodules and the gitlink
git add .gitmodules
git add "$NESTED_REL"
git commit -m "chore: add $NESTED_REL as submodule" || true

# Push changes to remote
echo
echo "Pushing branch $TARGET_BRANCH and submodule config to origin..."
git push origin "$TARGET_BRANCH"

echo
echo "Submodule added. To initialize/update submodules for clones run:"
echo "  git submodule update --init --recursive"
echo
echo "Imported as submodule. Backup branch created: $BACKUP_BRANCH"