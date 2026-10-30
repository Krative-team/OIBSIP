import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, confusion_matrix, classification_report

iris = load_iris()
X, y = iris.data, iris.target
feature_names, target_names = iris.feature_names, iris.target_names

df = pd.DataFrame(X, columns=feature_names)
df['species'] = y
df['species_name'] = df['species'].apply(lambda x: target_names[x])

print(f"Shape: {df.shape}")
print(df.dtypes)
print(df.isnull().sum())
print(df.describe())
print(df['species_name'].value_counts())

sns.pairplot(df, hue='species_name', diag_kind='kde')
plt.show()

fig, axes = plt.subplots(2, 2, figsize=(12,8))
for i, feature in enumerate(feature_names):
    ax = axes[i//2, i%2]
    sns.boxplot(data=df, x='species_name', y=feature, ax=ax)
plt.tight_layout()
plt.show()

plt.figure(figsize=(8,6))
sns.heatmap(df[feature_names].corr(), annot=True, cmap='coolwarm')
plt.show()

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, stratify=y)

models = {
    'Logistic Regression': LogisticRegression(max_iter=200),
    'KNN': KNeighborsClassifier(n_neighbors=5),
    'Random Forest': RandomForestClassifier(n_estimators=100, random_state=42)
}

trained = {}
for name, model in models.items():
    model.fit(X_train, y_train)
    y_pred = model.predict(X_test)
    acc = accuracy_score(y_test, y_pred)
    trained[name] = (model, y_pred)
    print(f"{name}: {acc:.4f}")

for name, (model, y_pred) in trained.items():
    print(f"\n{name}")
    print(f"Accuracy: {accuracy_score(y_test, y_pred):.4f}")
    cm = confusion_matrix(y_test, y_pred)
    sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', xticklabels=target_names, yticklabels=target_names)
    plt.title(f'Confusion Matrix - {name}')
    plt.show()
    print(classification_report(y_test, y_pred, target_names=target_names))

best = max(trained, key=lambda n: accuracy_score(y_test, trained[n][1]))
print(f"\nBest Model: {best}")

rf_model = trained['Random Forest'][0]
importances = rf_model.feature_importances_
feat_imp = pd.DataFrame({'feature': feature_names, 'importance': importances}).sort_values('importance', ascending=False)
sns.barplot(data=feat_imp, x='importance', y='feature')
plt.title('Feature Importance - Random Forest')
plt.show()
print(feat_imp)
