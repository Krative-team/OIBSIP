# TASK 1 · Iris Flower Classification
# Tech Stack: Python, sklearn, pandas, matplotlib, seaborn

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, confusion_matrix, classification_report

# 1. LOAD DATASET
iris = load_iris()
df = pd.DataFrame(iris.data, columns=iris.feature_names)
df['species'] = pd.Categorical.from_codes(iris.target, iris.target_names)
df['target'] = iris.target
print("Dataset loaded")
df.head()

# 2. EDA
print("\n=== EDA ===")
print("Shape:", df.shape)
print("\nDtypes:\n", df.dtypes)
print("\nNull check:\n", df.isnull().sum())
print("\nDescriptive Stats:\n", df.describe())
print("\nClass distribution:\n", df['species'].value_counts())

# 3. VISUALISATIONS
sns.set(style="whitegrid")

# Pairplot - shows feature distributions by species
plt.figure()
pair = sns.pairplot(df, hue='species', diag_kind='hist', palette='Set2')
pair.fig.suptitle("Pairplot - Feature Distributions by Species", y=1.02)
plt.show()

# Box plots for each feature
fig, axes = plt.subplots(2, 2, figsize=(12, 8))
features = iris.feature_names
for i, ax in enumerate(axes.flat):
    sns.boxplot(x='species', y=features[i], data=df, ax=ax, palette='Set2')
    ax.set_title(f"{features[i]} by species")
plt.tight_layout()
plt.show()

# Correlation heatmap
plt.figure(figsize=(6,4))
sns.heatmap(df[features].corr(), annot=True, cmap='coolwarm')
plt.title("Feature Correlation")
plt.show()

# 4. FEATURE SELECTION DISCUSSION
# From pairplot & boxplots:
# - Setosa is linearly separable, especially via petal length/width
# - Petal length and petal width are most discriminative (Versicolor vs Virginica overlap mainly in sepal features)
# - All 4 features are useful, but petal features have highest variance between species
# We will use all 4 for best accuracy, but petal_length + petal_width alone gives ~96% accuracy.

X = df[features]
y = df['target']

# 5. TRAIN/TEST SPLIT 80/20
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, stratify=y)
print(f"\nTrain: {X_train.shape}, Test: {X_test.shape}")

# 6. TRAIN 3 CLASSIFIERS
models = {
    "Logistic Regression": LogisticRegression(max_iter=200),
    "K-Nearest Neighbours (k=5)": KNeighborsClassifier(n_neighbors=5),
    "Random Forest": RandomForestClassifier(n_estimators=100, random_state=42)
}

results = {}
for name, model in models.items():
    model.fit(X_train, y_train)
    y_pred = model.predict(X_test)
    acc = accuracy_score(y_test, y_pred)
    results[name] = (model, y_pred, acc)
    print(f"\n--- {name} ---")
    print(f"Accuracy: {acc:.4f}")
    print("Classification Report:\n", classification_report(y_test, y_pred, target_names=iris.target_names))
    
    # Confusion Matrix
    cm = confusion_matrix(y_test, y_pred)
    plt.figure(figsize=(4,3))
    sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', xticklabels=iris.target_names, yticklabels=iris.target_names)
    plt.title(f"Confusion Matrix - {name}")
    plt.ylabel("True"); plt.xlabel("Predicted")
    plt.show()

# 7. BEST MODEL
print("\n=== MODEL COMPARISON ===")
for name, (_, _, acc) in results.items():
    print(f"{name}: {acc*100:.2f}%")

best_name = max(results, key=lambda k: results[k][2])
print(f"\nBest Performing Model: {best_name} with Accuracy {results[best_name][2]*100:.2f}%")
print("Justification: Highest accuracy, balanced precision/recall, robust confusion matrix. Random Forest usually wins due to non-linear separability of Versicolor vs Virginica.")

# Feature Importance from Random Forest
rf = results["Random Forest"][0]
importances = pd.Series(rf.feature_importances_, index=features).sort_values(ascending=False)
print("\nFeature Importance (Random Forest):\n", importances)

plt.figure(figsize=(6,3))
importances.plot(kind='bar')
plt.title("Feature Importance")
plt.ylabel("Importance")
plt.show()
