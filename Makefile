artifact_name       := document-generator
artifact_core_name  := document-generator-api
commit              := $(shell git rev-parse --short HEAD)
tag                 := $(shell git tag -l 'v*-rc*' --points-at HEAD)
version             := $(shell if [[ -n "$(tag)" ]]; then echo $(tag) | sed 's/^v//'; else echo $(commit); fi)
artifactory_publish := $(shell if [[ -n "$(tag)" ]]; then echo release; else echo dev; fi)

.PHONY: all
all: build

.PHONY: submodules
submodules:
	git submodule init
	git submodule update

.PHONY: clean
clean:
	mvn clean
	rm -f ./$(artifact_name).jar
	rm -f ./$(artifact_name)-*.zip
	rm -rf ./build-*

.PHONY: build
build: submodules
	mvn versions:set -DnewVersion=$(version) -DgenerateBackupPoms=false
	mvn package -DskipTests=true
	mv ./$(artifact_core_name)/target/$(artifact_core_name)-$(version).jar ./$(artifact_name).jar

.PHONY: test
test: test-unit

.PHONY: test-unit
test-unit: clean
	mvn test

.PHONY: package
package:
	@test -s ./$(artifact_name).jar || { echo "ERROR: Service JAR not found: $(artifact_name)"; exit 1; }
	$(eval tmpdir:=$(shell mktemp -d build-XXXXXXXXXX))
	cp ./$(artifact_name).jar $(tmpdir)
	cp ./start.sh $(tmpdir)
	cp ./routes.yaml $(tmpdir)
	mkdir $(tmpdir)/document-generator-api
	cp -r ./document-generator-api/api-enumerations $(tmpdir)/document-generator-api
	cd $(tmpdir); zip -r ../$(artifact_name)-$(version).zip *
	rm -rf $(tmpdir)

.PHONY: dist
dist: clean build package

.PHONY: sonar
sonar:
	mvn sonar:sonar

