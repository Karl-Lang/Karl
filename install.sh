#!/bin/bash

APP_NAME=Karl
INSTALL_DIR=/opt/$APP_NAME
JAVA_VERSION="19.0.1"
SCRIPT_FILE="$INSTALL_DIR/$APP_NAME.sh"
ALIAS_CMD="alias karl='sh $SCRIPT_FILE'"

RED_BOLD='\e[1m\e[31m'
WHITE_BOLD='\e[1m\e[37m'
RESET='\033[0m'

error() {
  echo -e "${RED_BOLD}ERROR: $1${RESET}"
  exit 1
}

info() {
  echo -e "${WHITE_BOLD}$1${RESET}"
}

if [[ "$OSTYPE" == "win"* ]] || [[ "$OSTYPE" == "WIN"* ]]; then
  error "Please use Windows Subsystem for linux, or use the script that correspond to Windows operating system."
fi

if ! command -v java &> /dev/null; then
  info "Java 17 is not installed, installing..."

  if [[ $(uname -s) == "Linux" ]]; then
      if [[ $(uname -m) == "x86_64" ]]; then
          os_arch="linux-x64"
      elif [[ $(uname -m) == "aarch64" ]]; then
          os_arch="linux-aarch64"
      else
          error "Unsupported architecture."
          exit 1
      fi
  elif [[ $(uname -s) == "Darwin" ]]; then
      os_arch="macos-x64"
  else
      error "Unsupported OS."
      exit 1
  fi

  info "Downloading JDK 17..."
  wget "https://download.oracle.com/java/17/archive/jdk-17.0.6_${os_arch}_bin.tar.gz" -O jdk.tar.gz || { error "An error has occured while downloading Java."; }

  info "Installing JDK 17..."
  sudo mkdir -p /opt/jdk
  sudo tar -xf jdk.tar.gz -C /opt/jdk --strip-components=1 || { error "An error has occured while installing JDK 17"; }

  info "Configuration of Java's environnement..."
  sudo update-alternatives --install /usr/bin/java java /opt/jdk/bin/java 1
  sudo update-alternatives --install /usr/bin/javac javac /opt/jdk/bin/javac 1
  sudo update-alternatives --set java /opt/jdk/bin/java
  sudo update-alternatives --set javac /opt/jdk/bin/javac

  rm jdk.tar.gz || { error "An error has occured while removing downloaded Java archive"; }

  if command -v java &>/dev/null; then
      info "Java installed with success"
  else
      error "Java was not installed. Please check by closing and re-opening your terminal."
  fi
else
  java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
      if [[ "$java_version" < "$JAVA_MIN_VERSION" ]]; then
          error "Java version $JAVA_MIN_VERSION or higher is required"
      fi
fi

info "Downloading Karl's latest version, please wait..."
wget -q --show-progress https://github.com/Karl-Lang/Karl/releases/latest/download/Karl.jar # || error "Can't download latest Karl release, please check your connection."

mkdir -p $INSTALL_DIR || error "Failed to create Karl's installation folder, please check your permissions."

cp $APP_NAME.jar $INSTALL_DIR/ || error "Failed to copy Karl's jar file into installation folder, please check your permissions."

bash_configs=(
    "$HOME/.bashrc"
    "$HOME/.bash_profile"
    "$HOME/.bash_aliases"
)

if [[ ${XDG_CONFIG_HOME:-} ]]; then
    bash_configs+=(
        "$XDG_CONFIG_HOME/.bash_profile"
        "$XDG_CONFIG_HOME/.bashrc"
        "$XDG_CONFIG_HOME/bash_profile"
        "$XDG_CONFIG_HOME/bashrc"
    )
fi

for config_file in "${bash_configs[@]}"; do
    if [[ -f "$config_file" ]]; then
        # shellcheck disable=SC2024
        echo "$ALIAS_CMD" > "$config_file"
    fi
done

source "$HOME/.bashrc"

rm $APP_NAME.jar || error "Failed to remove temporaly Karl's jar file."

echo "Installation terminée avec succès! Pour exécuter l'application, utilisez la commande 'karl' dans votre terminal."
