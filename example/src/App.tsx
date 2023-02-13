import * as React from 'react';

import { StyleSheet, Text, View } from 'react-native';
import ConnectivityChecker from 'react-native-connectivity-checker';

export default function App() {
  const [result, setResult] = React.useState<boolean>();

  React.useEffect(() => {
    ConnectivityChecker.isLocationEnabled().then((res) => {
      setResult(res);
    });

    const listener = ConnectivityChecker.addListener((res) => {
      setResult(res.status);
    });

    return () => {
      listener.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {String(result)}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
