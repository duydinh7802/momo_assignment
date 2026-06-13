JAVAC   = javac
JAVA    = java
SRC_DIR = src
OUT_DIR = out
MAIN    = Main
TEST    = test.TestRunner

SRC_FILES = \
	$(SRC_DIR)/constant/*.java \
	$(SRC_DIR)/exception/*.java \
	$(SRC_DIR)/model/*.java \
	$(SRC_DIR)/service/*.java \
	$(SRC_DIR)/command/*.java \
	$(SRC_DIR)/Main.java

TEST_FILES = $(SRC_FILES) $(SRC_DIR)/test/*.java

.PHONY: folder build run test clean

folder:
	@mkdir -p $(SRC_DIR)/model
	@mkdir -p $(SRC_DIR)/service
	@mkdir -p $(SRC_DIR)/command
	@mkdir -p $(SRC_DIR)/test
	@mkdir -p $(SRC_DIR)/constant
	@mkdir -p $(SRC_DIR)/exception
	@mkdir -p $(OUT_DIR)
	@echo "Folders created."

build: folder
	$(JAVAC) -d $(OUT_DIR) $(SRC_FILES)
	@echo "Build successful."

run: build
	$(JAVA) -cp $(OUT_DIR) $(MAIN)

test: folder
	$(JAVAC) -d $(OUT_DIR) $(TEST_FILES)
	$(JAVA) -cp $(OUT_DIR) $(TEST)

clean:
	@rm -rf $(OUT_DIR)
	@echo "Cleaned."
