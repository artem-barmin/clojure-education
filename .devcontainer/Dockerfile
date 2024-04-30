FROM --platform=linux/arm64v8 arm64v8/clojure:temurin-21-tools-deps-jammy

SHELL ["/bin/bash", "-c"]

# Configure apt
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y install --no-install-recommends apt-utils 2>&1

# Install git, process tools, lsb-release (common in install instructions for CLIs)
RUN apt-get -y install git procps lsb-release unzip 

# Clean up
RUN apt-get autoremove -y \
	&& apt-get clean -y \
	&& rm -rf /var/lib/apt/lists/*
ENV DEBIAN_FRONTEND=dialog

# Install 1Password CLI
COPY --from=1password/op:2 /usr/local/bin/op /usr/local/bin/op

# Get latest nodejs
RUN curl https://raw.githubusercontent.com/creationix/nvm/master/install.sh | bash 
RUN source ~/.nvm/nvm.sh && nvm install node

# Set the default shell to bash rather than sh
ENV SHELL /bin/bash