import SwiftUI
import shared   // дуже важливо!

// iosApp/ContentView.swift
import SwiftUI
import shared // Імпортуємо твій Kotlin-модуль

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController() // Викликаємо функцію з Kotlin!
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all) // Щоб Compose займав увесь екран
    }
}
